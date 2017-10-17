package my.web.site.siteFrontend.cms.entity;

import my.web.site.commons.cms.vals.ConstValues;

import java.util.List;

public class RecordSearchRequest {

    public static final int PAGE_SIZE = 10;

    private ConstValues.RecordType recordType;
    private String nameRegex;
    private String category;
    private String username;
    private String fileName;
    private List<String> tags;

    private Integer resultLimit;
    private Integer page;

    public ConstValues.RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(ConstValues.RecordType recordType) {
        this.recordType = recordType;
    }

    public String getNameRegex() {
        return nameRegex;
    }

    public void setNameRegex(String nameRegex) {
        this.nameRegex = nameRegex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getResultLimit() {
        return resultLimit;
    }

    public void setResultLimit(Integer resultLimit) {
        this.resultLimit = resultLimit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
