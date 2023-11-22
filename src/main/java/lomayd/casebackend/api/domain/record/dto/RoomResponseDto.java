package lomayd.casebackend.api.domain.record.dto;

import lomayd.casebackend.api.domain.record.Room;
import lombok.*;

import java.util.List;

public class RoomResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordListInfo {
        private String opponent;
        private List<RecordInfo> record;

        public static RoomResponseDto.RecordListInfo of(String opponent, List<RecordInfo> record) {
            return RecordListInfo.builder()
                    .opponent(opponent)
                    .record(record)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordInfo {

        private int id;
        private String title;
        private String summary;
        private long timestamp;
        private int length;
        private double positive;
        private double neutral;
        private double negative;

        public static RoomResponseDto.RecordInfo of(Room room) {
            return RecordInfo.builder()
                    .id(room.getRoom())
                    .title(room.getTitle())
                    .summary(room.getSummary())
                    .timestamp(room.getTimestamp())
                    .length(room.getLength())
                    .positive(room.getPositive())
                    .neutral(room.getNeutral())
                    .negative(room.getNegative())
                    .build();
        }
    }
}
