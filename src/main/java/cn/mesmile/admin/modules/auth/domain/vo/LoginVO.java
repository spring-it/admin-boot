package cn.mesmile.admin.modules.auth.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zb
 * @Description
 */
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("accessToken")
    private String accessToken;

    @ApiModelProperty("刷新token")
    private String refreshToken;

    @ApiModelProperty("过期时间，单位秒")
    private Integer expire;

    public LoginVO(){}

    public LoginVO(String accessToken,String refreshToken ,Integer expire){
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expire = expire;
    }

}
