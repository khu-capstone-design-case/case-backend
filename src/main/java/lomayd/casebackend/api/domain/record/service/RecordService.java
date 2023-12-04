package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.record.Record;
import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.record.dto.RecordRequestDto;
import lomayd.casebackend.api.domain.record.dto.RecordResponseDto;
import lomayd.casebackend.api.domain.record.repository.RecordRepository;
import lomayd.casebackend.api.domain.record.repository.RoomRepository;
import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.repository.TalkerRepository;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final RoomRepository roomRepository;
    private final TalkerRepository talkerRepository;
    private final TokenService tokenService;

    public RecordResponseDto.ScriptListInfo getScriptList(HttpServletRequest httpServletRequest, int id) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        Room room = roomRepository.findByUserAndRoom(user.getName(), id)
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 해당 음성 파일을 가지고 있지 않습니다."));

        List<Record> scriptList = recordRepository.findAllByRoom(room.getRoom());

        List<RecordResponseDto.ScriptInfo> scriptListInfo = new ArrayList<>();

        for(Record r : scriptList) {
            scriptListInfo.add(RecordResponseDto.ScriptInfo.of(r));
        }

        return RecordResponseDto.ScriptListInfo.of(room, scriptListInfo);
    }

    public void removeScriptList(HttpServletRequest httpServletRequest, int id) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        Room room = roomRepository.findByUserAndRoom(user.getName(), id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 해당 음성 파일을 가지고 있지 않습니다."));

        File file = new File("./record/" + room.getFileName());

        file.delete();

        roomRepository.delete(room);

        recordRepository.deleteAllByRoom(room.getRoom());

        roomRepository.delete(room);
    }

    public void analyzeRecord(Room room, Talker talker, String fileName, String user, int speakerNum, File file) {
        String url = "http://localhost:8132/api/record";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fileName", fileName);
        params.add("user", user);
        params.add("speakerNum", speakerNum);
        params.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();

        RecordResponseDto.RecordAnalysisResult response =
                restTemplate.exchange(url, HttpMethod.POST, request, RecordResponseDto.RecordAnalysisResult.class).getBody();

        room.setSummary(response.getSummary());
        room.setLength(response.getLength());
        room.setPoint(50 + response.getPositive() - response.getNegative());
        room.setPositive(response.getPositive());
        room.setNeutral(response.getNeutral());
        room.setNegative(response.getNegative());

        roomRepository.save(room);

        for(RecordResponseDto.ScriptInfo script : response.getMessage()) {
            Record record = Record.builder()
                    .room(room.getRoom())
                    .seq(script.getSeq())
                    .speaker(chooseSpeaker(room, script.getSpeaker()))
                    .message(script.getMessage())
                    .startTime(room.getTimestamp() + script.getStartTime())
                    .endTime(room.getTimestamp() + script.getEndTime())
                    .positive(script.getPositive())
                    .neutral(script.getNeutral())
                    .negative(script.getNegative())
                    .build();

            recordRepository.save(record);
        }

        int currentRecord = roomRepository.countByUser(user);

        talker.setLength(talker.getLength() + room.getLength());
        talker.setPoint((talker.getPoint() * (currentRecord-1) + room.getPoint()) / currentRecord);
        talker.setPositive((talker.getPositive() * (currentRecord-1) + room.getPositive()) / currentRecord);
        talker.setNeutral((talker.getNeutral() * (currentRecord-1) + room.getNeutral()) / currentRecord);
        talker.setNegative((talker.getNegative() * (currentRecord-1) + room.getNegative()) / currentRecord);

        talkerRepository.save(talker);
    }

    private String chooseSpeaker(Room room, String index) {
        if(index.equals("0")) {
            return room.getUser();
        }
        else {
            return room.getOpponent();
        }
    }

    public RecordResponseDto.ScriptAnalysisInfo analyzeScript(HttpServletRequest httpServletRequest, RecordRequestDto.ScriptAnalysisInput data) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        Room room = roomRepository.findByUserAndRoom(user.getName(), data.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 해당 음성 파일을 가지고 있지 않습니다."));

        List<String> script = new ArrayList<>();

        for(int s : data.getSeq()) {
            Record record = recordRepository.findByRoomAndSeq(room.getRoom(), s)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 순서(seq)가 존재하지 않습니다."));

            script.add(record.getMessage());
        }

        String url = "http://localhost:8132/api/script";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, Object> params = new HashMap<>();
        params.put("script", script);

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();

        RecordResponseDto.ScriptAnalysisResult response =
                restTemplate.exchange(url, HttpMethod.POST, request, RecordResponseDto.ScriptAnalysisResult.class).getBody();

        return RecordResponseDto.ScriptAnalysisInfo.of(data.getId(), data.getSeq(), response.getPositive(), response.getNeutral(), response.getNegative());
    }
}
