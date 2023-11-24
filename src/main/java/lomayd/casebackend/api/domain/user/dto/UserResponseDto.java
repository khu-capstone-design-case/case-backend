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
        private String id;

        public static UserResponseDto.UserLogin of(String accessToken, String id) {
            return UserLogin.builder()
                    .accessToken(accessToken)
                    .id(id)
                    .build();
        }
    }
}