package lomayd.casebackend.api.domain.record.controller;

import lomayd.casebackend.api.domain.record.dto.RecordResponseDto;
import lomayd.casebackend.api.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/api/user/page/record")
    public ResponseEntity<RecordResponseDto.ScriptListInfo> getScriptList(HttpServletRequest httpServletRequest, @RequestParam int id) {
        return ResponseEntity.ok(recordService.getScriptList(httpServletRequest, id));
    }

    @DeleteMapping("/api/user/page/record")
    public ResponseEntity<Void> removeScriptList(HttpServletRequest httpServletRequest, @RequestParam int id) throws UnsupportedEncodingException {
        recordService.removeScriptList(httpServletRequest, id);
        return ResponseEntity.ok().build();
    }
}
