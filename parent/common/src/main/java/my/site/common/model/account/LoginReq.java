package my.site.common.model.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginReq {

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
