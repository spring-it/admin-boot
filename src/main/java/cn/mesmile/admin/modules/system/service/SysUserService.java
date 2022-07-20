package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author zb
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查找用户信息表
     * @param page 分页信息
     * @return
     */
    Page<SysUser> findSysUserPage(Page<SysUser> page);
}

