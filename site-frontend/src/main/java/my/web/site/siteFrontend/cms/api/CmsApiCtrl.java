package my.web.site.siteFrontend.cms.api;

import my.web.site.commons.cms.dao.RecordRepo;
import my.web.site.commons.cms.model.Record;
import my.web.site.commons.cms.util.CmsUtil;
import my.web.site.commons.cms.vals.ConstValues;
import my.web.site.commons.commons.CommonResponse;
import my.web.site.commons.commons.CommonResponseBuilder;
import my.web.site.commons.user.model.UserInfo;
import my.web.site.siteFrontend.cms.entity.CmsServiceException;
import my.web.site.siteFrontend.cms.entity.NotFoundException;
import my.web.site.siteFrontend.cms.entity.RecordSearchRequest;
import my.web.site.siteFrontend.user.entity.UserAuthException;
import my.web.site.siteFrontend.user.util.UserApiUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static my.web.site.commons.commons.CommonsValues.ResponseStatus.Fail;
import static my.web.site.commons.commons.CommonsValues.ResponseStatus.OK;
import static my.web.site.commons.user.vals.ConstValues.Role.Admin;
import static my.web.site.siteFrontend.cms.api.CmsApiCtrl.BASE_URL;
import static my.web.site.siteFrontend.cms.entity.RecordSearchRequest.PAGE_SIZE;

@RestController
@RequestMapping(BASE_URL)
public class CmsApiCtrl {

    public static final String BASE_URL = "/cms";
    public static final String RECORD_SEARCH = "/public/recordSearch";
    public static final String RECORD_GET = "/public/record";
    public static final String RECORD_PRIVATE = "/private/record";
    public static final String FILE_GET = "/public/file";
    public static final String FILE_BROWSER = "/private/fileBrowser";
    public static final String FILE_PRIVATE = "/private/file";

    @Autowired
    private RecordRepo recordRepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    UserApiUtil userApiUtil;

    //Search
    @RequestMapping(RECORD_SEARCH)
    @ResponseBody
    public CommonResponse recordSearch(@RequestBody RecordSearchRequest request) {
        //组建查询
        Query query = new Query();
        //限制公开私有
        if(UserApiUtil.isAnonymous()){
            query.addCriteria(Criteria.where(Record.F_visibility).is(ConstValues.Visibility.Public));
        }else if(UserApiUtil.isUser()){
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            query.addCriteria(
                    Criteria.where(Record.F_visibility).is(ConstValues.Visibility.Public)
                            .orOperator(Criteria.where(Record.F_username).is(currentUsername))
            );
        }

        //完全匹配
        if(request.getRecordType() != null){
            query.addCriteria(Criteria.where(Record.F_recordType).is(request.getRecordType()));
        }
        if(request.getFileName() != null){
            query.addCriteria(Criteria.where(Record.F_blobId).is(request.getFileName()));
        }
        if(StringUtils.isNoneBlank(request.getUsername())){
            query.addCriteria(Criteria.where(Record.F_username).is(request.getUsername()));
        }
        if(StringUtils.isNoneBlank(request.getCategory())){
            query.addCriteria(Criteria.where(Record.F_category).is(request.getCategory()));
        }
        //by regex
        if(StringUtils.isNoneBlank(request.getNameRegex())){
            query.addCriteria(Criteria.where(Record.F_name).regex(Pattern.compile("%"+ request.getNameRegex() +"%")));
        }
        //any in collection
        if(request.getTags() != null && !request.getTags().isEmpty()){
            query.addCriteria(Criteria.where(Record.F_tags).in(request.getTags()));
        }
        //排除主要属性
        query.fields().exclude(Record.F_content);
        //分页，排序
        int pageLimit = request.getResultLimit() == null ? PAGE_SIZE : request.getResultLimit();
        int page = request.getPage() == null ? 0 : request.getPage();
        Pageable pageable = new PageRequest(page, pageLimit, new Sort(Sort.Direction.DESC, Record.F_createDate));
        query.with(pageable);

        //执行
        List<Record> result = mongoTemplate.find(query, Record.class, Record.class.getSimpleName().toLowerCase());

        //Pagination
        Page<Record> recordPage = PageableExecutionUtils.getPage(
                result,
                pageable,
                () -> mongoTemplate.count(query, Record.class)
        );
        //组装
        CommonResponse commonResponse = new CommonResponseBuilder()
                .setStatus(OK)
                .setData(recordPage)
                .createCommonResponse();

        return commonResponse;
    }

    //record CURD
    @RequestMapping(path = RECORD_GET, method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse recordGet(@RequestBody Record request) {
        if (request.getId() == null) {
            return new CommonResponse(Fail, "id can not be null");
        }
        Record record = recordRepo.findOne(request.getId());
        if (record == null) {
            return new CommonResponse(Fail, "Can not find record:" + request.getId());
        }
        if(!canAccess(userApiUtil.getUserInfoFromContext() , record)){
            return new CommonResponse(Fail, "Can not Access:");
        }
        return new CommonResponse(OK, null, null, record);
    }

    @RequestMapping(path = RECORD_PRIVATE, method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse recordUpdate(@RequestBody Record request) {
        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getName()) || request.getRecordType() == null) {
            return new CommonResponse(Fail, "Username/UserId/Name/recordType can not be null");
        }
        String currentUsername = userApiUtil.getUserInfoFromContext().getUsername();
        if (request.getId() != null) {
            if(!recordRepo.exists(request.getId())) {
                return new CommonResponse(Fail, "not an existing Record id");
            }
            Record record = recordRepo.findOne(request.getId());
            if(!canModify(record)){
                return new CommonResponse(Fail, "Not your record:" + record.getId());
            }
        }else{
            if(!request.getUsername().equals(currentUsername) && !UserApiUtil.isAdmin()){
                return new CommonResponse(Fail, "New record Username not match:" + request.getUsername() + "/" + currentUsername);
            }
        }
        //日期
        if (StringUtils.isBlank(request.getId())) {
            request.setCreateDate(System.currentTimeMillis());
        }
        request.setLastModifyDate(System.currentTimeMillis());
        //类型限制, 文件上传id置空//TODO file不会在这
        if(request.getRecordType() != ConstValues.RecordType.File || request.getId() == null){
            request.setBlobId(null);
        }
        //tag 类型
        if(request.getTags() == null){
            request.setTags(new ArrayList<>());
        }


        Record record = recordRepo.save(request);
        return new CommonResponse(OK, null, null, record);
    }

    @RequestMapping(path = RECORD_PRIVATE, method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResponse recordDelete(@RequestBody Record request) {
        if (StringUtils.isBlank(request.getId()) || !recordRepo.exists(request.getId())) {
            return new CommonResponse(Fail, "Author id null or can not be found");
        }
        Record record = recordRepo.findOne(request.getId());
        if(!canModify(record)){
            return new CommonResponse(Fail, "Can not Access:");
        }
        recordRepo.delete(request.getId());
        if(record.getBlobId() != null){
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where(ConstValues.F_filename).is(record.getBlobId())));
        }

        return new CommonResponse(OK, null, null, null);
    }

    //文件接口
    @RequestMapping(path = FILE_GET + "/{fileName:.+}", method = RequestMethod.GET)
    public Resource fileGet(HttpServletResponse response, @PathVariable String fileName) throws MissingServletRequestParameterException {
        Record record = recordRepo.findByBlobId(fileName);
        if (record == null) {
            throw new NotFoundException("Can not find record:" + fileName);
        }
        if(!canAccess(userApiUtil.getUserInfoFromContext(), record)){
            throw new UserAuthException("Can not access record");
        }

        return gridFsTemplate.getResource(fileName);
    }

    @RequestMapping(path = FILE_BROWSER)
    public String fileBrowser(@RequestParam("CKEditorFuncNum") String funcNum) throws MissingServletRequestParameterException {
        UserInfo userInfo = userApiUtil.getUserInfoFromContext();

        Page<Record> recordPage = recordRepo.findByUserIdAndRecordType(userInfo.getId(), ConstValues.RecordType.File, new PageRequest(0, 1));

        StringBuilder builder = new StringBuilder();
        builder.append("<script type=\"text/javascript\">");
        Boolean toSet = true;

        recordPage.forEach( r ->{
            if(r.getRecordType() == ConstValues.RecordType.File && toSet){
                builder.append("window.opener.CKEDITOR.tools.callFunction(" + funcNum + ",'" + "/site/cms/public/file/" + r.getBlobId() + "');");
            }
        });

        builder.append("</script>");

        return builder.toString();
    }

    @PostMapping(FILE_PRIVATE)
    @ResponseBody
    public String fileUpdate(@RequestParam("upload") MultipartFile file, @RequestParam("CKEditorFuncNum") String funcNum) throws IOException {
        if(file.isEmpty()){
            throw new CmsServiceException("File is empty");//TODO 需要返回错误原因
        }
        UserInfo userInfo = userApiUtil.getUserInfoFromContext();

        Record record = new Record();
        record.setName(file.getOriginalFilename());
        record.setUsername(userInfo.getUsername());
        record.setUserId(userInfo.getId());
        record.setRecordType(ConstValues.RecordType.File);
        record.setVisibility(ConstValues.Visibility.Public);
        record.setCreateDate(System.currentTimeMillis());

        String finalName = CmsUtil.genFileName(file.getOriginalFilename());
        gridFsTemplate.store(new ByteArrayInputStream(file.getBytes()), finalName);//, file.getContentType()
        record.setBlobId(finalName);
        record.setLastModifyDate(System.currentTimeMillis());
        Record newRecord = recordRepo.save(record);

        StringBuilder builder = new StringBuilder();
        builder.append("<script type=\"text/javascript\">");
        builder.append("window.parent.CKEDITOR.tools.callFunction(" + funcNum + ",'" + "/site/cms/public/file/"+ newRecord.getBlobId() + "','')");
        builder.append("</script>");

        return builder.toString();
    }

    private boolean canAccess(UserInfo userInfo, Record record){
        if(record.getVisibility() == ConstValues.Visibility.Private && userInfo.getRole() != Admin && !record.getUsername().equals(userInfo.getUsername())){
            return false;
        }else{
            return true;
        }
    }

    private boolean canModify(Record record){
        String currentUsername = userApiUtil.getUserInfoFromContext().getUsername();
        return record.getUsername().equals(currentUsername) || UserApiUtil.isAdmin();
    }

}
