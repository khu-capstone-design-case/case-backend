package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, Integer> {
}
