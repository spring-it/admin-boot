package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysRoleMenu;
import cn.mesmile.admin.modules.system.mapper.SysRoleMenuMapper;
import cn.mesmile.admin.modules.system.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 角色和菜单关联 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public Page<SysRoleMenu> findSysRoleMenuPage(Page<SysRoleMenu> page) {
        Page<SysRoleMenu> result = page(page);
        return result;
    }
}
