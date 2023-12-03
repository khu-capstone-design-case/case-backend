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
        private int length;
        private double point;
        private double positive;
        private double neutral;
        private double negative;

        public static TalkerResponseDto.TalkerInfo of(Talker talker) {
            return TalkerInfo.builder()
                    .id(talker.getId())
                    .opponent(talker.getOpponent())
                    .length(talker.getLength())
                    .point(talker.getPoint())
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
