package lomayd.casebackend.api.domain.record.dto;

import lombok.*;

public class RoomRequestDto {

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
    }
}
