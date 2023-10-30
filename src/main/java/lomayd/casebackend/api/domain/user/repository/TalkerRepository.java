package lomayd.casebackend.api.domain.user.repository;

import lomayd.casebackend.api.domain.user.Talker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkerRepository extends JpaRepository<Talker, Integer> {
}
