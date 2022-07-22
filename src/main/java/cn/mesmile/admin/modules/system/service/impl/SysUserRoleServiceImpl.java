package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysUserRole;
import cn.mesmile.admin.modules.system.mapper.SysUserRoleMapper;
import cn.mesmile.admin.modules.system.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 用户和角色关联 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public Page<SysUserRole> findSysUserRolePage(Page<SysUserRole> page) {
        Page<SysUserRole> result = page(page);
        return result;
    }
}
