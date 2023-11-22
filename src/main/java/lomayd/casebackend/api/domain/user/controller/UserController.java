package lomayd.casebackend.api.domain.user.controller;

import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.dto.UserRequestDto;
import lomayd.casebackend.api.domain.user.dto.UserResponseDto;
import lomayd.casebackend.api.domain.user.service.UserService;
import lomayd.casebackend.api.global.security.config.Token;
import lomayd.casebackend.api.global.security.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/api/user/join")
    public ResponseEntity<Void> joinUser(@RequestBody UserRequestDto.UserJoin data) {
        userService.joinUser(data);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<UserResponseDto.UserLogin> loginUser(@RequestBody UserRequestDto.UserLogin data) {

        if (userService.loginUser(data)) {
            return issueToken(data.getId());
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디 입니다.");
    }

    private ResponseEntity<UserResponseDto.UserLogin> issueToken(String id) {

        Token token = tokenService.generateToken(id, "USER");

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(30 * 24 * 60 * 60) // 만료 기한
                .path("/")
                .secure(true)
                .httpOnly(true)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Set-Cookie", refreshTokenCookie.toString());

        userService.storeRefreshToken(userService.getUserById(id), token.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders)
                .body(UserResponseDto.UserLogin.of(token.getAccessToken()));
    }

    @PostMapping("/api/user/token")
    public ResponseEntity<UserResponseDto.UserLogin> reissueToken(@CookieValue(value = "refreshToken") Cookie cookie) {

        tokenService.verifyToken(cookie.getValue());

        User user = userService.getUserByRefreshToken(cookie.getValue());

        return issueToken(user.getId());
    }

    @PostMapping("/api/user/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {

        User user = tokenService.getUserByToken(tokenService.resolveToken(httpServletRequest));

        userService.logout(user);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}