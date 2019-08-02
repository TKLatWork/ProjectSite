package my.site.account.test;

import my.site.account.App;
import my.site.common.model.account.Account;
import my.site.common.model.api.ApiResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//
public class AccountApiTest {

    @LocalServerPort
    int randomServerPort;
    @Autowired
    AccountTestUtil actUtil;

    @Before
    public void init(){
        actUtil.setPortWithDefHost(String.valueOf(randomServerPort));
    }

    @Test
    public void getAccountTest(){
        String actName = actUtil.adminAct.getAccountName();
        ApiResult<Account> result = actUtil.getAccount(actName);
        assertThat(result.getStatus()).isEqualTo(ApiResult.STATUS_OK);
        assertThat(result.getResult().getAccountName()).isEqualTo(actName);
    }


}
