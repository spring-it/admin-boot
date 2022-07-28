package cn.mesmile.admin.modules.auth.config;

import cn.mesmile.admin.common.config.system.AdminBootProperties;
import cn.mesmile.admin.modules.auth.filter.JwtAuthenticationTokenFilter;
import cn.mesmile.admin.modules.auth.security.AccessDeniedExceptionHandlerImpl;
import cn.mesmile.admin.modules.auth.security.AuthenticationEntryPointImpl;
import cn.mesmile.admin.modules.auth.security.LogoutSuccessHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zb
 * @Description 安全相关
 *  @EnableGlobalMethodSecurity(prePostEnabled = true)  开启基于注解的权限授权控制
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({AdminBootProperties.class})
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedExceptionHandlerImpl accessDeniedHandler;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;

    /*
     方式一和方式二 密码格式：
        {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        {noop}password
        {pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc
        {scrypt}$e0801$8bWJaSu2IKSn9Z9kM+TPXfOc/9bdYSrN1oD9qfVThWEwdRTnO7re7Ei+fUZRJ68k9lTyuTeUp4of4g24hHnazw==$OAOec05+bXxvuu/1qZ6NUR+xQYvYv7BeL1QxwRpY5Pc=
        {sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0
     */

    /**
     * 认证方式
     */
    @Bean
    public PasswordEncoder passwordEncoder(AdminBootProperties adminBootProperties){
        // 方式一：创建默认 DelegatingPasswordEncoder，支持多种加密认证的密码，默认 bcrypt
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 方式二：创建自定义 DelegatingPasswordEncoder ，以下【过期的类】为不安全的加密方式不推荐使用
        String encodingId = adminBootProperties.getPasswordEncoderType().getIdForEncode();
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
        encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256",new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);

        // 方式三：直接指定加密解密方式
        // 方式三密码格式 $2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
//        return new BCryptPasswordEncoder();
    }

    /**
     * 在接口中我们通过AuthenticationManager的authenticate方法来进行用户认证，
     * 所以需要在SecurityConfig中配置把 AuthenticationManager注入容器
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置 放行接口
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//         super.configure(http);
        http
                // 前后端分离 关闭 csrf
                .csrf().disable()
                // 前后端分离 session不管用   不通过 Session 获取 SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-resources",
                        "/v2/api-docs",
                        "/doc.html",
                        "/webjars/**/*.css",
                        "/webjars/**/*.js",
                        "/favicon.ico",
                        "/oauth/**").permitAll()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
                // 除了上面的请求，其他请求都需要鉴权认证
                .anyRequest().authenticated();

        // 将认证过滤器放在 UsernamePasswordAuthenticationFilter 之前
         http
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 将自定义的 认证 和 授权 异常处理加入到配置
        http.exceptionHandling()
                // 授权 403
                .accessDeniedHandler(accessDeniedHandler)
                // 认证 401
                .authenticationEntryPoint(authenticationEntryPoint);
        // 退出登录处理
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
    }

}