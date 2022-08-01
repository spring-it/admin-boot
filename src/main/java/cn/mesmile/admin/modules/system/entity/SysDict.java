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
 * 字典
 * </p>
 *
 * @author zb
 */
@Data
@TableName("sys_dict")
@ApiModel(value = "SysDict对象", description = "字典")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("父主键")
    private Long parentId;

    @ApiModelProperty("字典码")
    private String code;

    @ApiModelProperty("字典值")
    private String dictKey;

    @ApiModelProperty("字典名称")
    private String dictValue;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("字典备注")
    private String remark;

    @ApiModelProperty("是否已封存(0 未封存，1已封存)")
    private String sealed;

    @ApiModelProperty("是否已删除(0 未删除，1已删除)")
    @TableLogic
    private String deleted;


}
