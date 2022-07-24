package cn.mesmile.admin.modules.auth.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zb
 * @Description
 */
@Data
public class LoginRequest {

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
