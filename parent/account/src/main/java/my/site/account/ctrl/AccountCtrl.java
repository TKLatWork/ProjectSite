package my.site.account.ctrl;

import lombok.extern.slf4j.Slf4j;
import my.site.account.service.AccountService;
import my.site.account.service.JwtTokenProvider;
import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
import my.site.common.model.account.LoginReq;
import my.site.common.model.account.LoginResp;
import my.site.common.model.api.ApiException;
import my.site.common.services.AccountApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/account")
@RestController
@Slf4j
public class AccountCtrl implements AccountApi {

    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @GetMapping("/{id}")
    public Account get(@PathVariable String id) {
        return null;
    }

    @Override
    public Account update(@RequestBody Account account) {
        return null;
    }

    @Override
    public Account create(@RequestBody Account account) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
    }

    @PostMapping("/query")
    public List<Account> query(@RequestBody AccountQueryReq request) {
        return null;
    }

    @PostMapping("/login")
    public LoginResp login(@RequestBody LoginReq request) throws ApiException {
        Account account = accountService.getAccount(request.getUsername(), request.getPassword());
        if(account == null){
            throw new ApiException("账号或密码错误");
        }
        String jwtToken = jwtTokenProvider.createToken(account.getAccountName(), account.getAuth());
        return new LoginResp(jwtToken, account);
    }

}
