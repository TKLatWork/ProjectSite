package my.site.account.ctrl;

import com.google.gson.Gson;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequestMapping("/api/hateoas/account")
@RestController
@Slf4j
public class AccountHATEOASCtrl {

    @Autowired
    private AccountCtrl accountCtrl;

    @GetMapping(value = "/{id}",produces = { "application/hal+json" })
    public Account get(@PathVariable String id) {
        log.info("id:{}", id);
        AccountRes res = new AccountRes(accountCtrl.get(id));
        String json = new Gson().toJson(res, AccountRes.class);
        return null;
    }

}
