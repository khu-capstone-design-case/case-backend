package lomayd.casebackend.api.domain.record.controller;

import lomayd.casebackend.api.domain.record.dto.RoomResponseDto;
import lomayd.casebackend.api.domain.record.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/api/user/page/talker")
    public ResponseEntity<RoomResponseDto.RecordListInfo> getRecordList(HttpServletRequest httpServletRequest, @RequestParam String opponent) {
        return ResponseEntity.ok(roomService.getRecordList(httpServletRequest, opponent));
    }

    @DeleteMapping("/api/user/page/talker")
    public ResponseEntity<Void> removeRecordList(HttpServletRequest httpServletRequest, @RequestParam String opponent) {
        roomService.removeRecordList(httpServletRequest, opponent);
        return ResponseEntity.ok().build();
    }
}
