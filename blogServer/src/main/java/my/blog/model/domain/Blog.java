package my.blog.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Blog对象")
@Builder
@Data
public class Blog {

    private String id;
    @ApiModelProperty("拥有者的ID")
    private String ownerUserId;
    private String title;
    private String content;
    private Long createDate;
    private Long updateDate;

}
