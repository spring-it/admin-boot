package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户和角色关联 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 分页查找用户和角色关联
     * @param page 分页信息
     * @return
     */
    Page<SysUserRole> findSysUserRolePage(Page<SysUserRole> page);
}

