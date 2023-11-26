package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    @Query(value="{room: ?0}", sort= "{seq: 1}")
    List<Record> findAllByRoom(int room);

    @Query(value="{room: ?0}", delete = true)
    List<Record> deleteAllByRoom(int room);

    @Query(value="{room: ?0, seq:  ?1}")
    Optional<Record> findByRoomAndSeq(int room, int seq);
}
