package cn.mesmile.admin.modules.auth.filter;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.common.utils.AuthUtil;
import cn.mesmile.admin.modules.auth.security.LoginUserDetails;
import cn.mesmile.admin.modules.auth.security.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author zb
 * @Description
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private TokenService tokenService;
    @Resource
    private AdminRedisTemplate adminRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解析token
        String token = tokenService.getToken(request);
        if (StrUtil.isNotEmpty(token) && AuthUtil.getAuthentication() == null) {
            Claims claim = tokenService.getClaim(token);
            String uuid = (String) claim.get("uuid");
            String username = (String) claim.get("username");
            LoginUserDetails loginUserDetails = adminRedisTemplate.get(username + ":" + uuid);
            if (loginUserDetails != null){
                Collection<? extends GrantedAuthority> authorities = loginUserDetails.getAuthorities();
                SecurityContext securityContext = SecurityContextHolder.getContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDetails, null, authorities);
                // 将认证信息存入 SecurityContextHolder
                securityContext.setAuthentication(authenticationToken);
            }
        }
        // 放行
        filterChain.doFilter(request, response);
    }

}
