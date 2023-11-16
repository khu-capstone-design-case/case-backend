package lomayd.casebackend.api.domain.user.repository;

import lomayd.casebackend.api.domain.user.Talker;
import lomayd.casebackend.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalkerRepository extends JpaRepository<Talker, Integer> {

    List<Talker> findByUser(User user);
}
