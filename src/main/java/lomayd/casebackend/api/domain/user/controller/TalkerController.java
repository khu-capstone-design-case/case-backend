package lomayd.casebackend.api.domain.user.controller;

import lomayd.casebackend.api.domain.user.dto.TalkerResponseDto;
import lomayd.casebackend.api.domain.user.service.TalkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class TalkerController {

    private final TalkerService talkerService;

    @GetMapping
    public ResponseEntity<TalkerResponseDto.UserPageInfo> getUserPage(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(talkerService.getUserPage(httpServletRequest));
    }
}