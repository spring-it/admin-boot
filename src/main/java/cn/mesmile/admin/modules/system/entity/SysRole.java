package cn.mesmile.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author zb
 */
@Data
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("角色名")
    private String roleName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("角色编码")
    private String code;

    @ApiModelProperty("删除标志（0代表存在 1代表删除）")
    @TableLogic
    private String deleted;

    @ApiModelProperty("角色状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("备注")
    private String remark;


}
