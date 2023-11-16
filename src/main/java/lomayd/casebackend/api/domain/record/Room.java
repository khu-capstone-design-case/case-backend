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
    private String room;

    private String user;

    private String opponent;

    private String summary;

    private int length;

    private double positive;

    private double neutral;

    private double negative;
}
