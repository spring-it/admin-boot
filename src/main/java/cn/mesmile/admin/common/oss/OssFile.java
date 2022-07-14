package cn.mesmile.admin.common.oss;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zb
 * @Description
 */
@Data
public class OssFile {

    @ApiModelProperty("文件下载链接")
    private String url;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("hash值")
    private String hash;

    @ApiModelProperty("文件大小")
    private long length;

    @ApiModelProperty("上传时间")
    private LocalDateTime putTime;

    @ApiModelProperty("文件类型")
    private String contentType;
}
