package my.site.account.service;

import my.site.account.dao.AccountRepository;
import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
import my.site.common.model.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Account getAccount(String id){
        return accountRepo.getFirstById(id);
    }

    public Account getAccountByAccountName(String accountName){
        return accountRepo.getFirstByAccountName(accountName);
    }

    public Account getAccount(String accountName, String password){
        String encodedPassword = passwordEncoder.encode(password);
        return accountRepo.getFirstByAccountNameAndPassword(accountName, encodedPassword);
    }

    public Account createAccount(String name, String accountName, String rawPassword, List<String> auth) throws ApiException {
        if(accountRepo.existsByAccountName(accountName)){
            throw new ApiException("重复的账户名称");
        }
        Account newAct = new Account();
        newAct.setName(name);
        newAct.setAccountName(accountName);
        newAct.setPassword(passwordEncoder.encode(rawPassword));
        newAct.setAuth(auth);
        return accountRepo.save(newAct);
    }

}
