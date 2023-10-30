package lomayd.casebackend.api.domain.record.repository;

import lomayd.casebackend.api.domain.record.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepository extends MongoRepository<Record, String> {
}
