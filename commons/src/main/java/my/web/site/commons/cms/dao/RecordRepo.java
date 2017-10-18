package my.web.site.commons.cms.dao;

import my.web.site.commons.cms.model.Record;
import my.web.site.commons.cms.vals.ConstValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordRepo extends MongoRepository<Record, String> {

    Record findByBlobId(String blobId);
    void deleteByNameStartsWith(String prefix);

    Page<Record> findByUserId(String id, Pageable pageable);

    Page<Record> findByUserIdAndRecordType(String id, ConstValues.RecordType recordType, Pageable pageable);
}
