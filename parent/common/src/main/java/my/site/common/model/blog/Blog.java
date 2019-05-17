package my.site.common.model.blog;

import lombok.Data;

import java.util.List;

@Data
public class Blog {

    private String id;
    private String ownerAccountId;
    private String title;
    private List<String> tags;
    private String content;

}
