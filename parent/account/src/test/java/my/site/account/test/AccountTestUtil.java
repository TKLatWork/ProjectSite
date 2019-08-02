package my.site.account.test;

import my.site.account.config.AuthConfig;
import my.site.account.test.common.HttpClientUtil;
import my.site.common.model.account.Account;
import my.site.common.model.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 和Account相关的测试数据
 */
@Component
public class AccountTestUtil extends HttpClientUtil {

    @Autowired
    private AuthConfig authConfig;

    public Account adminAct;

    @PostConstruct
    public void init(){
        //系统自动创建的ADMIN
        adminAct = new Account();
        adminAct.setAccountName(authConfig.getAdminAccountName());
        adminAct.setPassword(authConfig.getAdminPassword());
    }

    public static final String URL_ROOT = "/api/account";
    public static final String GET_ACCOUNT_URL = URL_ROOT + "/%s";

    public ApiResult<Account> getAccount(String accountName){
        String url = String.format(GET_ACCOUNT_URL, accountName);
        return this.getPType(new ParameterizedTypeReference<ApiResult<Account>>() {}, url);
    }
}
