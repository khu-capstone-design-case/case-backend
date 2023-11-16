package lomayd.casebackend.api.domain.user.dto;

import lombok.*;

public class UserRequestDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserJoin {
        private String id;
        private String password;
        private String name;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private String id;
        private String password;
    }
}