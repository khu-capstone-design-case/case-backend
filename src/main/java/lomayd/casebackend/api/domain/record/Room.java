package lomayd.casebackend.api.domain.record;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="room")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String id;

    private int room;

    private String fileName;

    private int speakerNum;

    private String title;

    private String user;

    private String opponent;

    private String summary;

    private long timestamp;

    private int length;

    private double point;

    private double positive;

    private double neutral;

    private double negative;

    private int seq;
}
