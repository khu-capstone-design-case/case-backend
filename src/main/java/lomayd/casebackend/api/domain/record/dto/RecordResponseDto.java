package lomayd.casebackend.api.domain.record.dto;

import lomayd.casebackend.api.domain.record.Record;
import lomayd.casebackend.api.domain.record.Room;
import lomayd.casebackend.api.domain.user.User;
import lombok.*;

import java.util.List;

public class RecordResponseDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptListInfo {

        private int id;
        private String title;
        private String fileName;
        private List<ScriptInfo> script;

        public static RecordResponseDto.ScriptListInfo of(Room room, List<ScriptInfo> script) {
            return ScriptListInfo.builder()
                    .id(room.getRoom())
                    .title(room.getTitle())
                    .fileName(room.getFileName())
                    .script(script)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptInfo {

        private int seq;
        private String speaker;
        private String message;
        private long startTime;
        private long endTime;
        private double positive;
        private double neutral;
        private double negative;

        public static RecordResponseDto.ScriptInfo of(Record record) {
            return RecordResponseDto.ScriptInfo.builder()
                    .seq(record.getSeq())
                    .speaker(record.getSpeaker())
                    .message(record.getMessage())
                    .startTime(record.getStartTime())
                    .endTime(record.getEndTime())
                    .positive(record.getPositive())
                    .neutral(record.getNeutral())
                    .negative(record.getNegative())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordAnalysisResult {

        private String fileName;
        private String user;
        private int speakerNum;
        private int length;
        private double positive;
        private double neutral;
        private double negative;
        private String summary;
        private List<RecordResponseDto.ScriptInfo> message;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptAnalysisResult {

        private double positive;
        private double neutral;
        private double negative;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptAnalysisInfo {

        private int id;
        private List<Integer> seq;
        private double positive;
        private double neutral;
        private double negative;

        public static RecordResponseDto.ScriptAnalysisInfo of(int id, List<Integer> seq, double positive, double neutral, double negative) {
            return ScriptAnalysisInfo.builder()
                    .id(id)
                    .seq(seq)
                    .positive(positive)
                    .neutral(neutral)
                    .negative(negative)
                    .build();
        }
    }
}
