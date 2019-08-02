package my.site.account.dao;

import my.site.common.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Boolean existsByAccountName(String accountName);

    Account getFirstById(String id);

    Account getFirstByAccountName(String accountName);

    Account getFirstByAccountNameAndPassword(String accountName, String password);

    Page<Account> findAllBy(Pageable pageable);
}
