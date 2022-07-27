package cn.mesmile.admin.modules.auth.security;

import cn.mesmile.admin.common.constant.DataStatus;
import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.modules.system.entity.SysUser;
import cn.mesmile.admin.modules.system.mapper.SysMenuMapper;
import cn.mesmile.admin.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zb
 * @Description
 */
@RequiredArgsConstructor
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ISysUserService sysUserService;

    private final SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        if (sysUser == null){
            throw new ServiceException("用户名或密码错误");
        }
        String status = sysUser.getStatus();
        if (DataStatus.DISABLE.equals(status)){
            throw new ServiceException("账号已经停用，请联系管理员");
        }
        // 查询权限
        List<String> permsList = sysMenuMapper.selectPermsByUserId(sysUser.getId());
        return new LoginUserDetails(sysUser, permsList);
    }

}
