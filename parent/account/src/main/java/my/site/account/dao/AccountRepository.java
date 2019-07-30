package my.site.account.dao;

import my.site.common.model.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Account getFirstById(String id);

    Account getFirstByAccountName(String accountName);

    Account getFirstByAccountNameAndPassword(String accountName, String password);
}
