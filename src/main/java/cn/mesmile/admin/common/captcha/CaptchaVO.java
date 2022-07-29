package cn.mesmile.admin.common.captcha;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zb
 * @Description
 */
@NoArgsConstructor
@Data
public class CaptchaVO {

    public CaptchaVO(String key,String img){
        this.key = key;
        this.img = img;
    }

    @ApiModelProperty("唯一值")
    private String key;

    @ApiModelProperty("图像的base64")
    private String img;

}
