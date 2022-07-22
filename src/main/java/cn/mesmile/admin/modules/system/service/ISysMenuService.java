package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysMenu;
import cn.mesmile.admin.common.constant.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysMenuService extends IBaseService<SysMenu> {

    /**
     * 分页查找菜单权限表
     * @param page 分页信息
     * @return
     */
    Page<SysMenu> findSysMenuPage(Page<SysMenu> page);
}

