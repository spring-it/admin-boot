package cn.mesmile.admin.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zb
 * @Description
 *     字段自动填充
 *     填充判断:
 *          1、如果是主键，不填充
 *          2、根据字段名找不到字段，不填充
 *          3、字段类型与填充值类型不匹配，不填充
 *          4、[字段类型]需要在TableField注解里配置fill，
 *              没有配置或不匹配则不生效
 *              @TableField(value = "create_by", fill = FieldFill.INSERT)
 *              @TableField(value = "create_time", fill = FieldFill.INSERT)
 *              @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
 *              @TableField(value = "update_by", fill = FieldFill.UPDATE)
 */
@Component
public class MybatisPlusAutoFillHandler implements MetaObjectHandler {

    /**
     * 新增时填入值
     * @param metaObject
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = getUsername();
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        // 创建人的填充
        this.strictInsertFill(metaObject, "createBy", String.class, username);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    /**
     * 修改时填入值
     * @param metaObject
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        String username = getUsername();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        // 修改人的填充
        this.strictUpdateFill(metaObject, "updateBy", String.class, username);
    }

    /**
     * <获取安全上下文里的用户对象 --- 主要是在线程里面获取改值
     **/
    private String getUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
//        if (authentication != null) {
//            LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
//            username = loginUserDetails.getUsername();
//        }
        return username;
    }
}