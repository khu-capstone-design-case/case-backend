package lomayd.casebackend.api.domain.record.dto;

import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
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
        private double point;
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
                    .point(room.getPoint())
                    .positive(room.getPositive())
                    .neutral(room.getNeutral())
                    .negative(room.getNegative())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordUploadInfo {
        private int recordId;
        private int talkerId;
        private String fileName;
        private String userId;
        private int speakerNum;

        public static RoomResponseDto.RecordUploadInfo of(Room room, Talker talker, String path, User user, int speakerNum) {
            return RoomResponseDto.RecordUploadInfo.builder()
                    .recordId(room.getRoom())
                    .talkerId(talker.getId())
                    .fileName(path)
                    .userId(user.getId())
                    .speakerNum(speakerNum)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordProgressInfo {
        private int id;
        private int seq;

        public static RoomResponseDto.RecordProgressInfo of(int id, Room room) {
            return RecordProgressInfo.builder()
                    .id(id)
                    .seq(room.getSeq())
                    .build();
        }
    }
}
