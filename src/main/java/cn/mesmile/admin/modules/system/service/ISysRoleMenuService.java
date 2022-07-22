package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 角色和菜单关联 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 分页查找角色和菜单关联
     * @param page 分页信息
     * @return
     */
    Page<SysRoleMenu> findSysRoleMenuPage(Page<SysRoleMenu> page);
}

