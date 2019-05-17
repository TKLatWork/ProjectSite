package my.site.common.services;

import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;

import java.util.List;

/**
 * 账号控制的接口，
 * Login入口并不在这
 */
public interface AccountApi {

    /**
     * 获取账号信息，
     * @param id 当为空，获取当前账号
     * @return
     */
    Account get(String id);

    Account save(Account account);

    void delete(String id);

    List<Account> query(AccountQueryReq request);
}
