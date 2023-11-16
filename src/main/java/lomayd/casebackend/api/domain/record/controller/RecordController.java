package lomayd.casebackend.api.domain.record.controller;

import lomayd.casebackend.api.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;

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

    @GetMapping("/{fileName}")
    public Resource getRecord(HttpServletRequest httpServletRequest, @PathVariable String fileName) throws MalformedURLException {
        return recordService.getRecord(httpServletRequest, fileName);
    }
}
