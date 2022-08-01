package cn.mesmile.admin.modules.auth.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zb
 * @Description
 */
@Data
public class LoginRequest {

    @NotBlank(message = "用户名不允许为空")
    @ApiModelProperty("用户账号")
    private String username;

    @NotBlank(message = "密码不允许为空")
    @ApiModelProperty("密码")
    private String password;
}
