package my.site.account.service;

import my.site.account.dao.AccountRepository;
import my.site.common.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class DaoUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        Account account = accountRepo.getFirstByAccountName(accountName);
        UserDetails userDetails = null;
        if(account != null){
            List<String> authList = account.getAuth() == null? new ArrayList<>():account.getAuth();
            List<GrantedAuthority> authorities = authList.stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            userDetails = new User(account.getAccountName(), account.getPassword(), authorities);
        }
        return userDetails;
    }

}
