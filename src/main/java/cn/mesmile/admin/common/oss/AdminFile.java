package cn.mesmile.admin.common.oss;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zb
 * @Description
 */
@Data
public class AdminFile {

    @ApiModelProperty("文件下载链接")
    private String url;

    @ApiModelProperty("域名")
    private String domain;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件原名称")
    private String originalName;

    @ApiModelProperty("附件id")
    private Long attachId;
}
