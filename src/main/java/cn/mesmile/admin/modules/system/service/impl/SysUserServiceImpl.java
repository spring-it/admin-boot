package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysUser;
import cn.mesmile.admin.modules.system.mapper.SysUserMapper;
import cn.mesmile.admin.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.SUPPORTS,readOnly = true)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public Page<SysUser> findSysUserPage(Page<SysUser> page) {
        Page<SysUser> result = page(page);
        return result;
    }
}
