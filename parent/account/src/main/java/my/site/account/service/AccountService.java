package my.site.account.service;

import my.site.account.dao.AccountRepository;
import my.site.common.model.account.Account;
import my.site.common.model.account.AccountQueryReq;
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

    public Account getAccount(String accountName, String password){
        String encodedPassword = passwordEncoder.encode(password);
        return accountRepo.getFirstByAccountNameAndPassword(accountName, encodedPassword);
    }

}
