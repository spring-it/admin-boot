package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysMenu;
import cn.mesmile.admin.modules.system.mapper.SysMenuMapper;
import cn.mesmile.admin.modules.system.service.ISysMenuService;
import cn.mesmile.admin.common.constant.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public Page<SysMenu> findSysMenuPage(Page<SysMenu> page) {
        Page<SysMenu> result = page(page);
        return result;
    }
}
