package cn.mesmile.admin.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户和角色关联
 * </p>
 *
 * @author zb
 */
@Data
@TableName("sys_user_role")
@ApiModel(value = "SysUserRole对象", description = "用户和角色关联")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("角色ID")
    private Long roleId;

}
