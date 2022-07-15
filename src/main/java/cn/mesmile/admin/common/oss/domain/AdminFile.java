package cn.mesmile.admin.common.oss.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zb
 * @Description
 */
@Data
public class AdminFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /** https://play.min.io/resource/upload/202207/20220715/test.png */
    @ApiModelProperty("文件下载链接")
    private String url;

    /** https://play.min.io/resource */
    @ApiModelProperty("域名")
    private String domain;

    /** upload/202207/20220715/test.png */
    @ApiModelProperty("文件名")
    private String name;

    /**  test.png */
    @ApiModelProperty("文件原名称")
    private String originalName;

    @ApiModelProperty("附件id")
    private Long attachId;

    public AdminFile(){}

    public AdminFile(String url,String domain,String name,String originalName){
        this.url = url;
        this.domain = domain;
        this.name = name;
        this.originalName = originalName;
    }
}
