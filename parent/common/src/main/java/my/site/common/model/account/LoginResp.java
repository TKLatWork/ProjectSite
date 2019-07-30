package my.site.common.model.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp {

    @ApiModelProperty("JWT认证token")
    private String jwtToken;
    @ApiModelProperty("账户信息")
    private Account account;

}
