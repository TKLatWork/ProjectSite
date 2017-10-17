package my.web.site.commons.user.dao;

import my.web.site.commons.user.model.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


public interface UserInfoRepo extends MongoRepository<UserInfo, String> {

    UserInfo findByUsername(String username);
    UserInfo deleteByUsername(String username);
    void deleteByUsernameStartsWith(String prefix);

}
