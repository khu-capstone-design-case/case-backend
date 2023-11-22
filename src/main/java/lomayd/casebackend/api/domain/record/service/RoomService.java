package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.record.dto.RoomResponseDto;
import lomayd.casebackend.api.domain.record.repository.RecordRepository;
import lomayd.casebackend.api.domain.record.repository.RoomRepository;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.repository.TalkerRepository;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
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
public class RoomService {

    private final RoomRepository roomRepository;
    private final RecordRepository recordRepository;
    private final TalkerRepository talkerRepository;
    private final TokenService tokenService;

    private final String absolutePath = new File("").getAbsolutePath() + "/record/";
    private int recordNum = 1;

    public RoomResponseDto.RecordListInfo getRecordList(HttpServletRequest httpServletRequest, String opponent) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        List<Room> recordList = roomRepository.findAllByUserAndOpponent(user.getName(), opponent);

        List<RoomResponseDto.RecordInfo> recordListInfo = new ArrayList<>();

        for(Room r : recordList) {
            recordListInfo.add(RoomResponseDto.RecordInfo.of(r));
        }

        return RoomResponseDto.RecordListInfo.of(opponent, recordListInfo);
    }

    public void removeRecordList(HttpServletRequest httpServletRequest, String opponent) {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        List<Room> rooms = roomRepository.findAllByUserAndOpponent(user.getName(), opponent);

        for(Room r : rooms) {
            recordRepository.deleteAllByRoom(r.getRoom());

            File file = new File("./record/" + r.getFileName());

            file.delete();
        }

        roomRepository.deleteAllByUserAndOpponent(user.getName(), opponent);

        talkerRepository.deleteByOpponent(opponent);
    }

    public void uploadRecord(HttpServletRequest httpServletRequest, String opponent, int speakerNum, String title, MultipartFile file) throws IOException {
        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        String path = "record-" + recordNum + getFileExtension(file);

        Room room = Room.builder()
                .room(recordNum)
                .fileName(path)
                .speakerNum(speakerNum)
                .title(title)
                .user(user.getName())
                .opponent(opponent)
                .build();

        roomRepository.save(room);

        File tempFile = new File(absolutePath + path);
        tempFile.mkdirs();
        file.transferTo(tempFile);

        recordNum++;
    }

    private String getFileExtension(MultipartFile data) {
        String contentType = data.getContentType();

        if (ObjectUtils.isEmpty(contentType)) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "녹음 파일 확장자를 알 수 없습니다.");
        }

        if (contentType.contains("audio/mp4")) {
            return ".m4a";
        }

        if (contentType.contains("audio/mpeg")) {
            return ".mp3";
        }

        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "녹음 파일 확장자는 .m4a, .mp3만 가능합니다");
    }

    public Resource getRecord(String fileName) throws MalformedURLException {
        return new UrlResource("file:" + absolutePath + fileName);
    }
}
