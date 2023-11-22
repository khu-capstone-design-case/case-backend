package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.record.Record;
import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.record.dto.RecordResponseDto;
import lomayd.casebackend.api.domain.record.repository.RecordRepository;
import lomayd.casebackend.api.domain.record.repository.RoomRepository;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final RoomRepository roomRepository;
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
}
