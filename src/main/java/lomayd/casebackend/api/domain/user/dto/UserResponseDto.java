package lomayd.casebackend.api.domain.user.dto;

import lombok.*;

public class UserResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private String accessToken;

        public static UserResponseDto.UserLogin of(String accessToken) {
            return UserLogin.builder()
                    .accessToken(accessToken)
                    .build();
        }
    }
}