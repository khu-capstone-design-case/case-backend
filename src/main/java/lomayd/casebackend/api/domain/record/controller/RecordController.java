package lomayd.casebackend.api.domain.record.controller;

import lomayd.casebackend.api.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/voice")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<Void> uploadRecord(HttpServletRequest httpServletRequest, @RequestParam MultipartFile record) throws IOException {
        recordService.uploadRecord(httpServletRequest, record);
        return ResponseEntity.ok().build();
    }
}
