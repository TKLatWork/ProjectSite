package my.web.site.siteFrontend.user.api;

import my.web.site.commons.cms.util.CmsUtil;
import my.web.site.commons.commons.CommonResponse;
import my.web.site.commons.commons.CommonResponseBuilder;
import my.web.site.commons.commons.CommonsValues;
import my.web.site.commons.user.dao.UserInfoRepo;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.siteFrontend.user.entity.LoginRequest;
import my.web.site.siteFrontend.user.util.UserApiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static my.web.site.siteFrontend.user.api.UserApiCtrl.BASE_URL;

@Controller
@RequestMapping(BASE_URL)
public class UserApiCtrl {

    public static final String BASE_URL = "/user";
    public static final String CURRENT = "/public/current";
    public static final String LOGIN = "/public/login";
    public static final String LOGIN_SUCCESS = "/public/loginSuccess";
    public static final String LOGIN_FAIL = "/public/loginFail";
    public static final String LOGOUT = "/public/logout";
    public static final String LOGOUT_SUCCESS = "/public/logoutSuccess";
    public static final String ADMIN_DUMMY = "/admin/dummy";

    @Autowired
    private UserInfoRepo userInfoRepo;
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(CURRENT)
    @ResponseBody
    public CommonResponse onCurrent() throws IOException, ServletException {
        CommonResponseBuilder responseBuilder = new CommonResponseBuilder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!UserApiUtil.isAnonymous()){
            String username = authentication.getName();
            UserInfo userInfo = userInfoRepo.findByUsername(username);
            userInfo.setPassword(null);
            responseBuilder.setData(userInfo);
            responseBuilder.setStatus(CommonsValues.ResponseStatus.OK);
        }else{
            responseBuilder.setStatus(CommonsValues.ResponseStatus.Fail);
            responseBuilder.setMessage("Not yet login");
        }
        return responseBuilder.createCommonResponse();
    }

    @RequestMapping(LOGIN_SUCCESS)
    @ResponseBody
    public CommonResponse onLoginSuccess() throws IOException, ServletException {
//        CommonResponse response = new CommonResponseBuilder().createCommonResponse();
//        response.setStatus(CommonsValues.ResponseStatus.OK);
        return onCurrent();
    }

    @RequestMapping(LOGIN_FAIL)
    @ResponseBody
    public CommonResponse onLoginFail() {
        CommonResponse response = new CommonResponseBuilder().createCommonResponse();
        response.setStatus(CommonsValues.ResponseStatus.Fail);
        return response;
    }

    @RequestMapping(LOGOUT_SUCCESS)
    @ResponseBody
    public CommonResponse onLogoutSuccess() {
        CommonResponse response = new CommonResponseBuilder().createCommonResponse();
        response.setStatus(CommonsValues.ResponseStatus.OK);
        return response;
    }

    @RequestMapping(ADMIN_DUMMY)
    @ResponseBody
    public CommonResponse onAdminDummy() {
        //for test case
        CommonResponse response = new CommonResponseBuilder().createCommonResponse();
        response.setStatus(CommonsValues.ResponseStatus.OK);
        return response;
    }


}
