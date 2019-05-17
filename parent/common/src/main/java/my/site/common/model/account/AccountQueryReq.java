package my.site.common.model.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AccountQueryReq {

    @ApiModelProperty("角色筛选条件")
    private List<String> roles;

}
