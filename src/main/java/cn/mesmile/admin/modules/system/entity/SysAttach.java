package cn.mesmile.admin.modules.system.entity;

import cn.mesmile.admin.common.constant.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author zb
 */
@Data
@TableName("sys_attach")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@ApiModel(value = "SysAttach对象", description = "附件表")
public class SysAttach extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("附件下载地址")
    private String url;

    @ApiModelProperty("域名")
    private String domain;

    @ApiModelProperty("附件名称")
    private String name;

    @ApiModelProperty("附件原名称")
    private String originalName;

    @ApiModelProperty("文件拓展名")
    private String extension;

    @ApiModelProperty("文件大小")
    private Long attachSize;

}
