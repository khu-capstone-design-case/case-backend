package lomayd.casebackend.api.domain.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Talker {

    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String opponent;

    @Column
    private int length;

    @Column
    private double point;

    @Column
    private double positive;

    @Column
    private double neutral;

    @Column
    private double negative;
}