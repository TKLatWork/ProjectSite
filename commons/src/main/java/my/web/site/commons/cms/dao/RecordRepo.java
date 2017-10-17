package my.web.site.commons.cms.dao;

import my.web.site.commons.cms.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepo extends MongoRepository<Record, String> {

    Record findByBlobId(String blobId);
    void deleteByNameStartsWith(String prefix);
}
