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
        private String name;

        public static UserResponseDto.UserLogin of(String accessToken, String id, String name) {
            return UserLogin.builder()
                    .accessToken(accessToken)
                    .id(id)
                    .name(name)
                    .build();
        }
    }
}