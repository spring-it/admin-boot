package cn.mesmile.admin.modules.auth.security;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWTUtil;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.modules.auth.config.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zb
 * @Description
 */
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@Component
public class TokenService {

    private final JwtProperties jwtProperties;

    private final AdminRedisTemplate adminRedisTemplate;

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUserDetails loginUser) {
        String username = loginUser.getUsername();
        Integer expireTime = jwtProperties.getExpireTime();
        String uuid = IdUtil.fastSimpleUUID();
        loginUser.setUuid(uuid);
        // 存储用户
        adminRedisTemplate.setEx(username+":"+uuid, loginUser, Duration.ofSeconds(expireTime));
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("uuid",uuid);
        claims.put("username", username);
        claims.put("userId",loginUser.getSysUser().getId());
        return createToken(claims);
    }

    /**
     * 创建刷新令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createRefreshToken(LoginUserDetails loginUser) {
        String username = loginUser.getUsername();
        Integer refreshExpireTime = jwtProperties.getRefreshExpireTime();
        String uuid = IdUtil.fastSimpleUUID();
        loginUser.setUuid(uuid);
        // 存储用户
        adminRedisTemplate.setEx(username+":refresh:"+uuid, loginUser, Duration.ofSeconds(refreshExpireTime));
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("uuid",uuid);
        claims.put("username", username);
        claims.put("userId",loginUser.getSysUser().getId());
        claims.put("refresh","yes");
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足2880分钟(2天)，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
//    public void verifyToken(LoginUserDetails loginUser) {
//        long expireTime = loginUser.getExpireTime();
//        long currentTime = System.currentTimeMillis();
//        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
//            refreshToken(loginUser);
//        }
//    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
//    public void refreshToken(LoginUserDetails loginUser) {
//        loginUser.setLoginTime(System.currentTimeMillis());
//        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
//        // 根据uuid将loginUser缓存
//        String userKey = getTokenKey(loginUser.getUuid());
//        bootRedisCache.setEx(userKey, loginUser, expireTime, TimeUnit.MINUTES);
//    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
//    public void setUserAgent(LoginUserDetails loginUser) {
//        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
//        if (requestAttributes == null){
//            return;
//        }
//        HttpServletRequest request = requestAttributes.getRequest();
//        // User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36
//        UserAgent userAgent = UserAgentParser.parse(request.getHeader("User-Agent"));
//        String ip = IpUtil.getIpAddr(request);
//        loginUser.setIp(ip);
//        loginUser.setLoginLocation(AddressUtil.getRealAddressByIP(ip));
//        if(userAgent != null){
//            loginUser.setBrowser(userAgent.getBrowser().getName());
//            loginUser.setOs(userAgent.getOs().getName());
//        }
//    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
//                .setExpiration(DateUtil.offsetSecond(now, jwtProperties.getExpireTime()))
                .setIssuer("powered by admin")
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()).compact();
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims getClaim(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取指定的属性
     * @param token 令牌
     * @param key key
     * @return
     */
    public Object getClaimValue(String token,String key) {
        Claims claim = getClaim(token);
        return claim.get(key);
    }

    /**
     * 获取请求头上的额token
     *
     * @param request 请求
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenHeader());
        String tokenPrefix = jwtProperties.getTokenPrefix();
        if (StringUtils.isNotEmpty(token) && token.startsWith(tokenPrefix)) {
            token = token.replace(tokenPrefix, "").trim();
        }
        return token;
    }

    public boolean deleteToken(String token){
        Claims claim = getClaim(token);
        String uuid = (String) claim.get("uuid");
        String username = (String) claim.get("username");
        return adminRedisTemplate.del(username + ":" + uuid);
    }


}
