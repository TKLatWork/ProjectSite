package my.web.site.siteFrontend.user;

import my.web.site.commons.user.dao.UserInfoRepo;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.siteFrontend.user.entity.UserDetailsDecorator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = null;
        if(StringUtils.isNoneBlank(username) && (userInfo = userInfoRepo.findByUsername(username)) != null){
            UserDetails userDetails = new UserDetailsDecorator(userInfo);
            return userDetails;
        }else{
            throw new UsernameNotFoundException(username);
        }
    }
}
