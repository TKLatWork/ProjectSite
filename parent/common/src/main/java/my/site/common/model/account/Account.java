package my.site.common.model.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Account {

    @Id
    private String id;
    @ApiModelProperty("显示的名称")
    private String name;
    @Indexed(unique = true)
    @ApiModelProperty("登陆的名称")
    private String accountName;
    private String password;
    @ApiModelProperty("角色，（权限）")
    private List<String> auth;
    private Long lastLogin;

}
