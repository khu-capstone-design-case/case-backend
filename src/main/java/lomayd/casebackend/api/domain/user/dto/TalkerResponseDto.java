package lomayd.casebackend.api.domain.user.dto;

import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import lombok.*;

import java.util.List;

public class TalkerResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPageInfo {

        private String id;
        private String name;
        List<TalkerInfo> talker;

        public static TalkerResponseDto.UserPageInfo of(User user, List<TalkerInfo> talkerInfoList) {
            return TalkerResponseDto.UserPageInfo.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .talker(talkerInfoList)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkerInfo {

        private int id;
        private String opponent;
        private double positive;
        private double neutral;
        private double negative;

        public static TalkerResponseDto.TalkerInfo of(Talker talker) {
            return TalkerResponseDto.TalkerInfo.builder()
                    .id(talker.getId())
                    .opponent(talker.getOpponent())
                    .positive(talker.getPositive())
                    .neutral(talker.getNeutral())
                    .negative(talker.getNegative())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Opponent {
        private List<String> opponent;

        public static TalkerResponseDto.Opponent of(List<String> opponents) {
            return TalkerResponseDto.Opponent.builder()
                    .opponent(opponents)
                    .build();
        }
    }

}
