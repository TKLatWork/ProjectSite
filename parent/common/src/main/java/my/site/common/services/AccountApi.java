package my.site.common.services;

import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
import my.site.common.model.account.LoginReq;
import my.site.common.model.account.LoginResp;
import my.site.common.model.api.ApiException;
import my.site.common.model.api.ApiResult;

import java.util.List;

public interface AccountApi {

    /**
     *
     * @param id 空时获取当前账号
     * @return
     */
    Account get(String id);

    Account update(Account account);

    Account create(Account account);

    void delete(String id);

    List<Account> query(AccountQueryReq request);

    LoginResp login(LoginReq request) throws ApiException;

}
