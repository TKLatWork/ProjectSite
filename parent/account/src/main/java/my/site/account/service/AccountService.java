package my.site.account.service;

import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    public Account get(String id){
        //TODO 过滤字段
        return null;
    }

    public Account save(Account account){
        return null;
    }

    public void delete(String id) {

    }

    public List<Account> query(AccountQueryReq request) {
        return null;
    }

}
