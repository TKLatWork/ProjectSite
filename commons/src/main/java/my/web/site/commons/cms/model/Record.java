package my.web.site.commons.cms.model;

import my.web.site.commons.cms.vals.ConstValues;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Record {

    public static final String F_name = "name";
    public static final String F_category = "category";
    public static final String F_tags = "tags";
    public static final String F_username = "username";
    public static final String F_createDate = "createDate";
    public static final String F_visibility = "visibility";
    public static final String F_recordType = "recordType";
    public static final String F_content = "content";
    public static final String F_blobId = "blobId";

    @Id
    private String id;
    private String name;
    @Indexed
    private String category;
    private List<String> tags;
    private String userId;
    @Indexed
    private String username;
    @Indexed
    private Long createDate;
    private Long lastModifyDate;
    private ConstValues.Visibility visibility;
    private ConstValues.RecordType recordType;

    private String content;
    private String blobId;//TODO fileName ?

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Long lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public ConstValues.Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(ConstValues.Visibility visibility) {
        this.visibility = visibility;
    }

    public ConstValues.RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(ConstValues.RecordType recordType) {
        this.recordType = recordType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBlobId() {
        return blobId;
    }

    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }
}
