package cn.mesmile.admin.modules.system.mapper;

import cn.mesmile.admin.modules.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author zb
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     *  通过用户id 查询权限
     * @param userId
     * @return
     */
    List<String> selectPermsByUserId(Long userId);

}
