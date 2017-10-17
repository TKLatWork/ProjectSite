package my.web.site.siteFrontend.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import my.web.site.commons.cms.model.Record;
import my.web.site.commons.cms.vals.ConstValues;
import my.web.site.commons.commons.CommonResponse;
import my.web.site.commons.commons.CommonsValues;
import my.web.site.siteFrontend.SiteFrontendApp;
import my.web.site.siteFrontend.cms.api.CmsApiCtrl;
import my.web.site.siteFrontend.test.util.MockMvcBaseTest;
import my.web.site.siteFrontend.test.util.TestDataUtil;
import my.web.site.siteFrontend.user.api.UserApiCtrl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static my.web.site.siteFrontend.test.util.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CMS的CURD功能和查询功能
 */
public class CmsApiTest extends MockMvcBaseTest {

    private Logger LOG = LoggerFactory.getLogger(CmsApiTest.class);

    /**
     * 可以查看公开的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void queryRecord() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_TEXT_ID);

        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_GET)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
        .andDo(r -> {
            JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
            CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
            Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

            assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
            assert record.getId().equals(RECORD_ADMIN_TEXT_ID);
        });
    }

    /**
     * 管理员可以查看私有记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void queryRecordPrivateAdmin() throws Exception {
        Record query = new Record();
        query.setId(RECORD_USER_TEXT_ID);

        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_GET)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getId().equals(RECORD_USER_TEXT_ID);
                });
    }

    /**
     * 可以查看自己的私有记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void queryRecordPrivateSelf() throws Exception {
        Record query = new Record();
        query.setId(RECORD_USER_TEXT_ID);

        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_GET)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getId().equals(RECORD_USER_TEXT_ID);
                });
    }

    /**
     * 无法查看私有记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void queryRecordPrivate() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_FILE_OLD_ID);

        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_GET)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.Fail;
                    assert jsonResponse.get("data").isJsonNull();
                });
    }

    /**
     * 无法修改不是自己的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void updateRecordUpdateNotSelf() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_FILE_NEW_ID);
        query.setName(RECORD_ADMIN_FILE_NEW.getName() + "_Modified");
        query.setUsername(RECORD_ADMIN_FILE_NEW.getUsername());
        query.setUserId(RECORD_ADMIN_FILE_NEW.getUserId());
        query.setRecordType(RECORD_ADMIN_FILE_NEW.getRecordType());

        mvc.perform(post(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.Fail;
                    assert jsonResponse.get("data").isJsonNull();
                });
    }

    /**
     * 管理员可以修改不是自己的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void updateRecordUpdateNotSelfAdmin() throws Exception {
        Record query = new Record();
        query.setId(RECORD_USER_TEXT_ID);
        query.setName(RECORD_USER_TEXT.getName() + "_Modified");
        query.setUsername(RECORD_USER_TEXT.getUsername());
        query.setUserId(RECORD_USER_TEXT.getUserId());
        query.setRecordType(RECORD_USER_TEXT.getRecordType());

        mvc.perform(post(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getId().equals(query.getId());
                    assert record.getName().equals(query.getName());
                });
    }


    /**
     * 可以修改自己的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void updateRecordUpdateSelf() throws Exception {
        Record query = new Record();
        query.setId(RECORD_USER_TEXT_ID);
        query.setName(RECORD_USER_TEXT.getName() + "_Modified");
        query.setUsername(RECORD_USER_TEXT.getUsername());
        query.setUserId(RECORD_USER_TEXT.getUserId());
        query.setRecordType(RECORD_USER_TEXT.getRecordType());

        mvc.perform(post(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getId().equals(query.getId());
                    assert record.getName().equals(query.getName());
                });
    }

    /**
     * 可以创建新记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void updateRecordAdd() throws Exception {
        Record query = new Record();
        String newRecordName = PREFIX + "NewRecord";
        query.setName(newRecordName);
        query.setUsername(TestDataUtil.USER_NAME);
        query.setUserId(USER.getId());
        query.setRecordType(ConstValues.RecordType.Article);

        mvc.perform(post(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getName().equals(query.getName());
                });
    }


    /**
     * 可以删除自己的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void deleteRecord() throws Exception {
        Record query = new Record();
        query.setId(RECORD_USER_TEXT_ID);

        mvc.perform(delete(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                });
    }

    /**
     * 不能删除别人的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.USER_NAME, authorities = {"User"})
    public void deleteRecordNotSelf() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_TEXT_ID);

        mvc.perform(delete(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.Fail;
                });
    }

    /**
     * 管理员可以删除别人的记录
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void deleteRecordNotSelfAdmin() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_TEXT_ID);

        mvc.perform(delete(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                });
    }

    /**
     * 文件 Record 删除后，文件不可访问
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void deleteRecordFile() throws Exception {
        Record query = new Record();
        query.setId(RECORD_ADMIN_FILE_OLD_ID);

        mvc.perform(delete(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                });
        //try to get image
        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_GET + "/" + RECORD_ADMIN_OLD_FIN_FILENAME)
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    /**
     * get 文件png
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void getFile() throws Exception {
        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_GET + "/" + RECORD_ADMIN_OLD_FIN_FILENAME)
        ).andExpect(status().isOk())
        .andExpect(content().contentType("image/png"));
    }

    /**
     * 无法重复上传
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void uploadFileExist() throws Exception {

        //upload file
        File newFile = new File(this.getClass().getClassLoader().getResource(RECORD_ADMIN_NEW_FILENAME).getFile());
        InputStream in = new ByteArrayInputStream(FileUtils.readFileToByteArray(newFile));
        MockMultipartFile fileMp = new MockMultipartFile("file", PREFIX + newFile.getName(), "multipart/form-data", in);
        mvc.perform(fileUpload(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_PRIVATE + "/" + RECORD_ADMIN_OLD_FIN_FILENAME)
                .file(fileMp)
        ).andExpect(status().is5xxServerError());

        //check file can get
        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_GET + "/" + RECORD_ADMIN_OLD_FIN_FILENAME)
        ).andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
    }

    /**
     * 创建Record并完成上传
     * @throws Exception
     */
    @Test
    @WithMockUser(username = TestDataUtil.ADMIN_NAME, authorities = {"Admin"})
    public void addRecordAndFile() throws Exception {
        Record query = new Record();
        String newRecordName = PREFIX + "NewRecord";
        query.setName(newRecordName);
        query.setUsername(TestDataUtil.USER_NAME);
        query.setUserId(USER.getId());
        query.setRecordType(ConstValues.RecordType.File);

        mvc.perform(post(CmsApiCtrl.BASE_URL + CmsApiCtrl.RECORD_PRIVATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(query))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getName().equals(query.getName());

                    query.setId(record.getId());
                });

        //upload file
        File newFile = new File(this.getClass().getClassLoader().getResource(RECORD_ADMIN_NEW_FILENAME).getFile());
        InputStream in = new ByteArrayInputStream(FileUtils.readFileToByteArray(newFile));
        MockMultipartFile fileMp = new MockMultipartFile("file", PREFIX + newFile.getName(), "multipart/form-data", in);
        mvc.perform(fileUpload(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_PRIVATE + "/" + query.getId())
                .file(fileMp)
        ).andExpect(status().isOk())
                .andDo(r -> {
                    JsonObject jsonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), JsonObject.class);
                    CommonResponse commonResponse = new Gson().fromJson(r.getResponse().getContentAsString(), CommonResponse.class);
                    Record record = new Gson().fromJson(jsonResponse.get("data"), Record.class);

                    assert commonResponse.getStatus() == CommonsValues.ResponseStatus.OK;
                    assert record.getName().equals(query.getName());
                    assert StringUtils.isNoneBlank(record.getBlobId());

                    query.setBlobId(record.getBlobId());
                });

        //check file can get
        mvc.perform(get(CmsApiCtrl.BASE_URL + CmsApiCtrl.FILE_GET + "/" + query.getBlobId())
        ).andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
    }

}
