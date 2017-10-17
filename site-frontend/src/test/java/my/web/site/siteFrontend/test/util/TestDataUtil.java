package my.web.site.siteFrontend.test.util;

import my.web.site.commons.cms.dao.RecordRepo;
import my.web.site.commons.cms.model.Record;
import my.web.site.commons.cms.util.CmsUtil;
import my.web.site.commons.user.dao.UserInfoRepo;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.commons.user.vals.ConstValues;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static my.web.site.commons.cms.vals.ConstValues.F_filename;
import static my.web.site.commons.cms.vals.ConstValues.RecordType.Article;
import static my.web.site.commons.cms.vals.ConstValues.RecordType.File;
import static my.web.site.commons.cms.vals.ConstValues.Visibility.Private;
import static my.web.site.commons.cms.vals.ConstValues.Visibility.Public;

@Service
public class TestDataUtil {

    public static final String PREFIX = "Test";

    public static final String ADMIN_NAME = PREFIX + "Admin";
    public static final String USER_NAME = PREFIX + "User";

    public static final String RECORD_USER_TEXT_ID = PREFIX + "RECORD_USER_TEXT";
    public static final String RECORD_ADMIN_TEXT_ID = PREFIX + "RECORD_ADMIN_TEXT";
    public static final String RECORD_ADMIN_FILE_NEW_ID = PREFIX + "RECORD_ADMIN_FILE_NEW";
    public static final String RECORD_ADMIN_FILE_OLD_ID = PREFIX + "RECORD_ADMIN_FILE";

    public static final String RECORD_ADMIN_OLD_FILENAME = "adminOldFile.png";
    public static final String RECORD_ADMIN_NEW_FILENAME = "adminNewFile.png";

    public static final String RECORD_ADMIN_OLD_FIN_FILENAME = PREFIX + CmsUtil.genFileName(RECORD_ADMIN_OLD_FILENAME);
    public static final String RECORD_ADMIN_NEW_FIN_FILENAME = PREFIX + CmsUtil.genFileName(RECORD_ADMIN_NEW_FILENAME);

    public static final UserInfo ADMIN;
    public static final UserInfo USER;

    public static final Record RECORD_USER_TEXT;
    public static final Record RECORD_ADMIN_TEXT;
    public static final Record RECORD_ADMIN_FILE_NEW;
    public static final Record RECORD_ADMIN_FILE;

    static {
        ADMIN = new UserInfo();
        ADMIN.setId(ADMIN_NAME + "_ID");
        ADMIN.setUsername(ADMIN_NAME);
        ADMIN.setPassword("123");
        ADMIN.setRole(ConstValues.Role.Admin);

        USER = new UserInfo();
        USER.setId(USER_NAME + "_ID");
        USER.setUsername(USER_NAME);
        USER.setPassword("123");
        USER.setRole(ConstValues.Role.User);

        RECORD_USER_TEXT = new Record();
        RECORD_USER_TEXT.setId(RECORD_USER_TEXT_ID);
        RECORD_USER_TEXT.setName(RECORD_USER_TEXT_ID);
        RECORD_USER_TEXT.setUsername(USER.getUsername());
        RECORD_USER_TEXT.setUserId(USER.getId());
        RECORD_USER_TEXT.setContent("Some not so long text content");
        RECORD_USER_TEXT.setCreateDate(new Date(0).getTime());
        RECORD_USER_TEXT.setLastModifyDate(new Date(100).getTime());
        RECORD_USER_TEXT.setTags(Arrays.asList(PREFIX, "user's"));
        RECORD_USER_TEXT.setVisibility(Private);
        RECORD_USER_TEXT.setCategory(null);
        RECORD_USER_TEXT.setRecordType(Article);
        RECORD_USER_TEXT.setBlobId(null);

        RECORD_ADMIN_TEXT = new Record();
        RECORD_ADMIN_TEXT.setId(RECORD_ADMIN_TEXT_ID);
        RECORD_ADMIN_TEXT.setName(RECORD_ADMIN_TEXT_ID);
        RECORD_ADMIN_TEXT.setUsername(ADMIN.getUsername());
        RECORD_ADMIN_TEXT.setUserId(ADMIN.getId());
        RECORD_ADMIN_TEXT.setContent("Some not so long text content");
        RECORD_ADMIN_TEXT.setCreateDate(new Date(0).getTime());
        RECORD_ADMIN_TEXT.setLastModifyDate(new Date(100).getTime());
        RECORD_ADMIN_TEXT.setTags(Arrays.asList(PREFIX, "admin's"));
        RECORD_ADMIN_TEXT.setVisibility(Public);
        RECORD_ADMIN_TEXT.setCategory(null);
        RECORD_ADMIN_TEXT.setRecordType(Article);
        RECORD_ADMIN_TEXT.setBlobId(null);

        RECORD_ADMIN_FILE_NEW = new Record();
        RECORD_ADMIN_FILE_NEW.setId(RECORD_ADMIN_FILE_NEW_ID);
        RECORD_ADMIN_FILE_NEW.setName(RECORD_ADMIN_FILE_NEW_ID);
        RECORD_ADMIN_FILE_NEW.setUsername(ADMIN.getUsername());
        RECORD_ADMIN_FILE_NEW.setUserId(ADMIN.getId());
        RECORD_ADMIN_FILE_NEW.setContent(null);
        RECORD_ADMIN_FILE_NEW.setCreateDate(new Date(0).getTime());
        RECORD_ADMIN_FILE_NEW.setLastModifyDate(new Date(100).getTime());
        RECORD_ADMIN_FILE_NEW.setTags(Arrays.asList(PREFIX, "admin's"));
        RECORD_ADMIN_FILE_NEW.setVisibility(Public);
        RECORD_ADMIN_FILE_NEW.setCategory(null);
        RECORD_ADMIN_FILE_NEW.setRecordType(File);
        RECORD_ADMIN_FILE_NEW.setBlobId(RECORD_ADMIN_NEW_FIN_FILENAME);

        RECORD_ADMIN_FILE = new Record();
        RECORD_ADMIN_FILE.setId(RECORD_ADMIN_FILE_OLD_ID);
        RECORD_ADMIN_FILE.setName(RECORD_ADMIN_FILE_OLD_ID);
        RECORD_ADMIN_FILE.setUsername(ADMIN.getUsername());
        RECORD_ADMIN_FILE.setUserId(ADMIN.getId());
        RECORD_ADMIN_FILE.setContent(null);
        RECORD_ADMIN_FILE.setCreateDate(new Date(0).getTime());
        RECORD_ADMIN_FILE.setLastModifyDate(new Date(100).getTime());
        RECORD_ADMIN_FILE.setTags(Arrays.asList(PREFIX, "admin's"));
        RECORD_ADMIN_FILE.setVisibility(Private);
        RECORD_ADMIN_FILE.setCategory(null);
        RECORD_ADMIN_FILE.setRecordType(File);
        RECORD_ADMIN_FILE.setBlobId(RECORD_ADMIN_OLD_FIN_FILENAME);

    }

    @Autowired
    private UserInfoRepo userInfoRepo;
    @Autowired
    private RecordRepo recordRepo;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void setup() throws IOException {
        clean();
        setupUserData();
        setupCmsData();
    }

    public void clean() {
        userInfoRepo.deleteByUsernameStartsWith(PREFIX);
        recordRepo.deleteByNameStartsWith(PREFIX);
        Query query = new Query().addCriteria(Criteria.where(F_filename).regex("^" + PREFIX));
        gridFsTemplate.delete(query);
    }

    public void setupUserData(){
        userInfoRepo.save(ADMIN);
        userInfoRepo.save(USER);
    }

    public void setupCmsData() throws IOException {
        recordRepo.save(RECORD_USER_TEXT);
        recordRepo.save(RECORD_ADMIN_TEXT);
        recordRepo.save(RECORD_ADMIN_FILE_NEW);
        recordRepo.save(RECORD_ADMIN_FILE);

        File oldFile = new File(this.getClass().getClassLoader().getResource(RECORD_ADMIN_OLD_FILENAME).getFile());
        InputStream in = new ByteArrayInputStream(FileUtils.readFileToByteArray(oldFile));
        gridFsTemplate.store(in, RECORD_ADMIN_OLD_FIN_FILENAME);//TODO 忽略了content type

        File newFile = new File(this.getClass().getClassLoader().getResource(RECORD_ADMIN_NEW_FILENAME).getFile());
        in = new ByteArrayInputStream(FileUtils.readFileToByteArray(newFile));
        gridFsTemplate.store(in, RECORD_ADMIN_NEW_FIN_FILENAME);
    }

}
