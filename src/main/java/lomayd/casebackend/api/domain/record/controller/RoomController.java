package lomayd.casebackend.api.domain.record.controller;

import lomayd.casebackend.api.domain.record.dto.RoomRequestDto;
import lomayd.casebackend.api.domain.record.dto.RoomResponseDto;
import lomayd.casebackend.api.domain.record.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;

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

    @PostMapping("/api/record/upload/init")
    public ResponseEntity<RoomResponseDto.RecordUploadInfo> uploadRecordInit(HttpServletRequest httpServletRequest, @RequestParam String opponent, @RequestParam int speakerNum, @RequestParam String title, @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(roomService.uploadRecordInit(httpServletRequest, opponent, speakerNum, title, file));
    }

    @PostMapping("/api/record/upload/analyze")
    public ResponseEntity<Void> uploadRecordAnalyze(@RequestBody RoomRequestDto.RecordUploadInfo data) {
        roomService.uploadRecordAnalyze(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/api/record/{fileName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Resource getRecord(@PathVariable String fileName) throws MalformedURLException {
        return roomService.getRecord(fileName);
    }
}
