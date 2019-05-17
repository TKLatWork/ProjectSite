package my.site.account.model;

import lombok.Data;
import my.site.common.model.account.Account;
import org.springframework.hateoas.ResourceSupport;

@Data
public class AccountRes extends ResourceSupport {

    private Account r;

    public AccountRes(Account r) {
        this.r = r;
    }
}
