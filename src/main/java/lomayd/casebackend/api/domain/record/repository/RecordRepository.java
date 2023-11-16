package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
}
