package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.record.dto.RoomRequestDto;
import lomayd.casebackend.api.domain.record.dto.RoomResponseDto;
import lomayd.casebackend.api.domain.record.repository.RecordRepository;
import lomayd.casebackend.api.domain.record.repository.RoomRepository;
import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.repository.TalkerRepository;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final RecordRepository recordRepository;
    private final TalkerRepository talkerRepository;
    private final RecordService recordService;
    private final TokenService tokenService;

    private final String absolutePath = new File("").getAbsolutePath() + "/record/";
    private int recordNum = 1;
    private int talkerNum = 1;

    public RoomResponseDto.RecordListInfo getRecordList(HttpServletRequest httpServletRequest, String opponent) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        List<Room> recordList = roomRepository.findAllByUserAndOpponent(user.getId(), opponent);

        List<RoomResponseDto.RecordInfo> recordListInfo = new ArrayList<>();

        for(Room r : recordList) {
            recordListInfo.add(RoomResponseDto.RecordInfo.of(r));
        }

        return RoomResponseDto.RecordListInfo.of(opponent, recordListInfo);
    }

    public void removeRecordList(HttpServletRequest httpServletRequest, String opponent) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        List<Room> rooms = roomRepository.findAllByUserAndOpponent(user.getId(), opponent);

        for(Room r : rooms) {
            recordRepository.deleteAllByRoom(r.getRoom());

            File file = new File("./record/" + r.getFileName());

            file.delete();
        }

        roomRepository.deleteAllByUserAndOpponent(user.getId(), opponent);

        talkerRepository.deleteByOpponent(opponent);
    }

    public RoomResponseDto.RecordUploadInfo uploadRecordInit(HttpServletRequest httpServletRequest, String opponent, int speakerNum, String title, MultipartFile file) throws IOException {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        String path = "record-" + recordNum + getFileExtension(file);

        File tempFile = new File(absolutePath + path);
        tempFile.mkdirs();
        file.transferTo(tempFile);

        Room room = Room.builder()
                .room(recordNum)
                .fileName(path)
                .speakerNum(speakerNum)
                .title(title)
                .user(user.getId())
                .opponent(opponent)
                .timestamp(tempFile.lastModified())
                .build();

        roomRepository.save(room);

        Talker talker;

        if(talkerRepository.existsByOpponent(opponent)) {
            talker = talkerRepository.findByOpponent(opponent);
        }
        else {
            talker = Talker.builder()
                    .id(talkerNum)
                    .user(user)
                    .opponent(opponent)
                    .length(0)
                    .point(0)
                    .positive(0)
                    .neutral(0)
                    .negative(0)
                    .build();

            talkerRepository.save(talker);
        }
        recordNum++;
        talkerNum++;

        return RoomResponseDto.RecordUploadInfo.of(room, talker, path, user, speakerNum);
    }

    @Async
    public void uploadRecordAnalyze(RoomRequestDto.RecordUploadInfo data) {
        recordService.analyzeRecord(
                roomRepository.findByUserAndRoom(data.getUserId(), data.getRecordId()).get(),
                talkerRepository.findById(data.getTalkerId()).get(),
                data.getFileName(),
                data.getUserId(),
                data.getSpeakerNum(),
                new File(absolutePath + data.getFileName())
        );
    }

    private String getFileExtension(MultipartFile data) {
        String contentType = data.getContentType();

        if (ObjectUtils.isEmpty(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "녹음 파일 확장자를 알 수 없습니다.");
        }

        if (contentType.contains("audio/x-m4a")) {
            return ".m4a";
        }

        if (contentType.contains("audio/mpeg")) {
            return ".mp3";
        }

        if (contentType.contains("audio/wav")) {
            return ".wav";
        }

        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "녹음 파일 확장자는 .m4a, .mp3, .wav만 가능합니다");
    }

    public Resource getRecord(String fileName) throws MalformedURLException {
        return new UrlResource("file:" + absolutePath + fileName);
    }

    public void addRecordUploadProgress(RoomRequestDto.RecordProgressUploadInfo data) {

        Room room = roomRepository.findByUserAndFileName(data.getUser(),data.getFileName())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자 또는 녹음 파일이 존재 하지 않습니다."));

        room.setSeq(data.getSeq());

        roomRepository.save(room);
    }

    public RoomResponseDto.RecordProgressInfo getRecordUploadProgress(HttpServletRequest httpServletRequest, int id) {

        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        Room room = roomRepository.findByUserAndRoom(user.getId(), id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자 또는 녹음 파일이 존재 하지 않습니다."));

        return RoomResponseDto.RecordProgressInfo.of(id, room);
    }
}
