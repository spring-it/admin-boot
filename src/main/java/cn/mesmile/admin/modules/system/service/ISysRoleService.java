package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查找角色
     * @param page 分页信息
     * @return
     */
    Page<SysRole> findSysRolePage(Page<SysRole> page);
}

