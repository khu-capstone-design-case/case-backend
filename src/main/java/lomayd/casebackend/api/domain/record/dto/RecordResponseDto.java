package lomayd.casebackend.api.domain.record.dto;

import lomayd.casebackend.api.domain.record.Record;
import lomayd.casebackend.api.domain.record.Room;
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
}
