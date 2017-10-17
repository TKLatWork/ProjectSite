package my.web.site.siteFrontend.user.config;

import my.web.site.commons.user.dao.UserInfoRepo;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.commons.user.vals.ConstValues;
import my.web.site.siteFrontend.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserConfig {

    @Autowired
    private UserInfoRepo userInfoRepo;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void setup(){
        if(userInfoRepo.findByUsername(appConfig.getAdminUserName()) == null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(appConfig.getAdminUserName());
            userInfo.setPassword(passwordEncoder.encode(appConfig.getAdminPassword()));
            userInfo.setRole(ConstValues.Role.Admin);

            userInfoRepo.save(userInfo);
        }
    }

}
