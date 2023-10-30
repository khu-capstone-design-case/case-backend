package lomayd.casebackend.api.domain.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String refreshToken;
}
