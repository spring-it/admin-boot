package cn.mesmile.admin.modules.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.constant.DataStatus;
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
import cn.mesmile.admin.modules.system.mapper.SysMenuMapper;
import cn.mesmile.admin.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private  ISysUserService sysUserService;
    @Resource
    private  SysMenuMapper sysMenuMapper;

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
        String refreshToken = tokenService.createRefreshToken(loginUserDetails);
        String tokenPrefix = jwtProperties.getTokenPrefix();
        return new LoginVO(tokenPrefix + " " +token,refreshToken ,jwtProperties.getExpireTime());
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

    @Override
    public LoginVO refreshToken(String refreshToken) {
        Claims claim = tokenService.getClaim(refreshToken);
        String uuid = (String) claim.get("uuid");
        String username = (String) claim.get("username");
        String refresh = (String) claim.get("refresh");
        if (StrUtil.isEmpty(refresh)){
            throw new BusinessException("非法token");
        }
        LoginUserDetails loginUserDetails = adminRedisTemplate.get(username + ":refresh:" + uuid);
        if (loginUserDetails == null){
            throw new BusinessException("token过期，请重新登录");
        }
        String token = tokenService.createToken(loginUserDetails);
        String newRefreshToken = tokenService.createRefreshToken(loginUserDetails);
        String tokenPrefix = jwtProperties.getTokenPrefix();
        // 删除原有刷新token
        adminRedisTemplate.del(username + ":refresh:" + uuid);
        return new LoginVO(tokenPrefix + " " +token,newRefreshToken ,jwtProperties.getExpireTime());
    }

}
