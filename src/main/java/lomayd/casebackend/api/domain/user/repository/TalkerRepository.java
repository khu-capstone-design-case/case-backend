package lomayd.casebackend.api.domain.user.repository;

import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkerRepository extends JpaRepository<Talker, Integer> {

    @Query("SELECT t FROM Talker t WHERE t.user = :user ORDER BY t.id ASC")
    List<Talker> findByUser(User user);

    @Query("SELECT t.opponent FROM Talker t WHERE t.user = :user")
    List<String> findOpponents(User user);

    void deleteByOpponent(String opponent);
}
