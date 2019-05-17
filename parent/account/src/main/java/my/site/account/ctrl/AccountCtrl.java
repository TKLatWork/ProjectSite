package my.site.account.ctrl;

import lombok.extern.slf4j.Slf4j;
import my.site.account.model.AccountRes;
import my.site.account.service.AccountService;
import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
import my.site.common.services.AccountApi;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/rest/account")
@RestController
@Slf4j
public class AccountCtrl implements AccountApi {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public Account get(String id) {
        log.info("id:{}", id);
        return accountService.get(id);
    }

    @PutMapping
    public Account save(Account request) {
        log.info("request:", ToStringBuilder.reflectionToString(request));
        return accountService.save(request);
    }

    @DeleteMapping
    public void delete(String id) {
        log.info("id:{}", id);
        accountService.delete(id);
    }

    @PostMapping("/query")
    public List<Account> query(AccountQueryReq request) {
        log.info("request:", ToStringBuilder.reflectionToString(request));
        return accountService.query(request);
    }

}
