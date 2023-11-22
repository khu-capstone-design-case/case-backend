package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, Integer> {

    @Query("{user: ?0, opponent: ?1}")
    List<Room> findAllByUserAndOpponent(String user, String opponent);
}
