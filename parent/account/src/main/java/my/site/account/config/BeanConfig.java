package my.site.account.config;

import my.site.account.dao.AccountRepository;
import my.site.account.service.AccountService;
import my.site.common.model.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;

@Configuration
public class BeanConfig {

    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void initData() throws ApiException {
        //创建管理员
        if(accountService.getAccountByAccountName(authConfig.getAdminAccountName()) == null){
            accountService.createAccount("admin",
                    authConfig.getAdminAccountName(),
                    authConfig.getAdminPassword(),
                    null);
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST: ");
        return filter;
    }

}
