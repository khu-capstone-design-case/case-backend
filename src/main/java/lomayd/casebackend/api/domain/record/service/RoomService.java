package lomayd.casebackend.api.domain.record.service;

import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.record.dto.RoomResponseDto;
import lomayd.casebackend.api.domain.record.repository.RecordRepository;
import lomayd.casebackend.api.domain.record.repository.RoomRepository;
import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RecordRepository recordRepository;
    private final TokenService tokenService;

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
        }

        roomRepository.deleteAllByUserAndOpponent(user.getName(), opponent);
    }
}
