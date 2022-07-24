package cn.mesmile.admin.modules.auth.service.impl;

import cn.mesmile.admin.common.exceptions.BusinessException;
import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.modules.auth.config.JwtProperties;
import cn.mesmile.admin.modules.auth.domain.request.LoginRequest;
import cn.mesmile.admin.modules.auth.domain.vo.LoginVO;
import cn.mesmile.admin.modules.auth.security.LoginUserDetails;
import cn.mesmile.admin.modules.auth.security.TokenService;
import cn.mesmile.admin.modules.auth.service.ILoginService;
import cn.mesmile.admin.modules.system.entity.SysUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zb
 * @Description
 */
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenService tokenService;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private AdminRedisTemplate adminRedisTemplate;

    @Override
    public LoginVO login(LoginRequest loginRequest) {
        String principal = loginRequest.getUsername();
        String credentials = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        // 用户认证 AuthenticationManager authenticate 进行用户认证
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BusinessException("用户名或密码错误");
        } catch (Exception e){
            throw new ServiceException("登录认证失败",e);
        }
        LoginUserDetails loginUserDetails = (LoginUserDetails) authenticate.getPrincipal();
        String token = tokenService.createToken(loginUserDetails);
        String tokenPrefix = jwtProperties.getTokenPrefix();
        return new LoginVO(tokenPrefix + " " +token, jwtProperties.getExpireTime());
    }

    @Override
    public boolean logout() {
        // 调用注销接口的时候需要携带token
        // 从 SecurityContextHolder 请求中获取认证信息，然后再获取username
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null){
            return false;
        }
        // 认证之后 principal 里面是 UserDetails 的子类
        // 未认证的时候 principal 里面是 username (登录账号)
        Object principal = authentication.getPrincipal();
        // UserLogin 实现了 UserDetails 接口
        LoginUserDetails userLogin = (LoginUserDetails) principal;
        SysUser user = userLogin.getSysUser();
        String username = user.getUsername();
        String uuid = userLogin.getUuid();
        // 认证设置为空
        context.setAuthentication(null);
        // 删除redis中的token
        return adminRedisTemplate.del(username+":"+uuid);
    }

}
