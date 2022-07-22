package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.common.constant.BaseServiceImpl;
import cn.mesmile.admin.modules.system.entity.SysUser;
import cn.mesmile.admin.modules.system.mapper.SysUserMapper;
import cn.mesmile.admin.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public Page<SysUser> findSysUserPage(Page<SysUser> page) {
        Page<SysUser> result = page(page);
        return result;
    }
}
