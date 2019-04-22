package my.blog.model.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "用户对象")
@Builder
@Data
public class User {

    private String id;
    private String name;
    private String pwd;

}
