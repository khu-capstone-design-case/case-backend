package lomayd.casebackend.api.domain.record.dto;

import lombok.*;

import java.util.List;

public class RecordRequestDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScriptAnalysisInput {
        private int id;
        private List<Integer> seq;
    }
}
