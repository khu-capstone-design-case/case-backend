package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, Integer> {

    @Query(value="{user: ?0, opponent: ?1}")
    List<Room> findAllByUserAndOpponent(String user, String opponent);

    @Query(value="{user: ?0, opponent: ?1}", delete=true)
    List<Room> deleteAllByUserAndOpponent(String user, String opponent);

    @Query(value="{user: ?0, room: ?1}")
    Optional<Room> findByUserAndRoom(String user, int room);
}
