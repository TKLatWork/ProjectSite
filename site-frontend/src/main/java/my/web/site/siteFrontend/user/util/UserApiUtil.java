package my.web.site.siteFrontend.user.util;

import my.web.site.commons.user.dao.UserInfoRepo;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.commons.user.vals.ConstValues;
import my.web.site.siteFrontend.user.entity.UserDetailsDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserApiUtil {

    @Autowired
    private UserInfoRepo userInfoRepo;

    public UserInfo getUserInfoFromContext(){
        UserInfo userInfo = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            userInfo = userInfoRepo.findByUsername(authentication.getName());
        }

        return userInfo;
    }

    public static boolean isAnonymous(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication != null && !authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS")));
    }

    public static boolean isUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ConstValues.Role.User.toString())));
    }

    public static boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(ConstValues.Role.Admin.toString())));
    }
}
