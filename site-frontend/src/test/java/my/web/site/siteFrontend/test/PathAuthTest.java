package my.web.site.siteFrontend.test;

import com.google.gson.Gson;
import my.web.site.commons.commons.CommonResponse;
import my.web.site.commons.commons.CommonsValues;
import my.web.site.siteFrontend.test.util.MockMvcBaseTest;
import my.web.site.siteFrontend.test.util.TestDataUtil;
import my.web.site.siteFrontend.user.api.UserApiCtrl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 基本的URL规则权限，直接使用Authority
 */
public class PathAuthTest extends MockMvcBaseTest {

    private Logger LOG = LoggerFactory.getLogger(PathAuthTest.class);

    @Test
    @WithAnonymousUser
    public void visitorCurrent() throws Exception {
        mvc.perform(post(UserApiCtrl.BASE_URL + UserApiCtrl.CURRENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isOk())
        .andDo( r -> {
            CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
            assert commonResponse.getStatus() == CommonsValues.ResponseStatus.Fail;
        });
    }

    @Test
    @WithAnonymousUser
    public void visitorPublic() throws Exception {
        mvc.perform(post("/user/public/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/public/logoutSuccess"));
    }

    @Test
    @WithAnonymousUser
    public void visitorPrivate() throws Exception {
        mvc.perform(post("/cms/private/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void visitorAdmin() throws Exception {
        mvc.perform(post(UserApiCtrl.BASE_URL + UserApiCtrl.ADMIN_DUMMY)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void userCurrent() throws Exception {
        mvc.perform(post(UserApiCtrl.BASE_URL + UserApiCtrl.CURRENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isOk())
                .andDo( r -> {
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                });
    }

    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void userPublic() throws Exception {
        mvc.perform(post("/user/public/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/public/logoutSuccess"));
    }

    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void userPrivate() throws Exception {
        mvc.perform(post("/cms/private/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isOk())
                .andDo(r -> {
                    CommonResponse response = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    assert response.getStatus() == CommonsValues.ResponseStatus.Fail;
                });
    }

    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void userAdmin() throws Exception {
        mvc.perform(post(UserApiCtrl.BASE_URL + UserApiCtrl.ADMIN_DUMMY)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void adminPublic() throws Exception {
        mvc.perform(post("/user/public/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/public/logoutSuccess"));
    }

    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void adminPrivate() throws Exception {
        mvc.perform(post("/cms/private/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isOk())
                .andDo(r -> {
                    CommonResponse response = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    assert response.getStatus() == CommonsValues.ResponseStatus.Fail;
                });
    }

    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void adminAdmin() throws Exception {
        mvc.perform(post(UserApiCtrl.BASE_URL + UserApiCtrl.ADMIN_DUMMY)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(status().isOk());
    }
}
