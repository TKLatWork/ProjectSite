package my.site.common.model.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Account {

    private String id;
    @ApiModelProperty("显示的名称")
    private String name;
    @ApiModelProperty("登陆的名称")
    private String accountName;
    private String password;
    @ApiModelProperty("角色，（权限）")
    private List<String> roles;
    private Long lastLogin;

}
