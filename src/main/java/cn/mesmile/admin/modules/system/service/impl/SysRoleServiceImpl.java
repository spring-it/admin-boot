package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysRole;
import cn.mesmile.admin.modules.system.mapper.SysRoleMapper;
import cn.mesmile.admin.modules.system.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public Page<SysRole> findSysRolePage(Page<SysRole> page) {
        Page<SysRole> result = page(page);
        return result;
    }
}
