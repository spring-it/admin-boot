package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysUser;
import cn.mesmile.admin.common.constant.IBaseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * 分页查找用户信息
     * @param page 分页信息
     * @return
     */
    Page<SysUser> findSysUserPage(Page<SysUser> page);
}

