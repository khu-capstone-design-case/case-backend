package lomayd.casebackend.api.domain.record;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="record")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @Id
    private String id;

    private int room;

    private int seq;

    private String speaker;

    private String message;

    private double startTime;

    private double endTime;

    private double positive;

    private double neutral;

    private double negative;
}