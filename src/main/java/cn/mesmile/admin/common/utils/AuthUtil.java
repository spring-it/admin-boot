package cn.mesmile.admin.common.utils;

import cn.mesmile.admin.common.exceptions.BusinessException;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.modules.auth.security.LoginUserDetails;
import cn.mesmile.admin.modules.system.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author zb
 * @Description 认证工具类
 */
public class AuthUtil {

    private AuthUtil(){}

    public static SysUser getSysUser(){
        Authentication authentication = getAuthentication();
        if (authentication != null){
            // 认证之后 principal 里面是 UserDetails 的子类
            // 未认证的时候 principal 里面是 username (登录账号)
            Object principal = authentication.getPrincipal();
            // UserLogin 实现了 UserDetails 接口
            LoginUserDetails userLogin = (LoginUserDetails) principal;
            return userLogin.getSysUser();
        }
        throw new BusinessException(ResultCode.FAILURE,"获取用户信息失败");
    }

    public static Long getUserId(){
        SysUser sysUser = getSysUser();
        if (sysUser != null){
            return sysUser.getId();
        }
        throw new BusinessException(ResultCode.FAILURE,"获取用户信息失败");
    }

    public static Authentication getAuthentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

//    public static boolean isAdmin(){
//
//
//    }

}
