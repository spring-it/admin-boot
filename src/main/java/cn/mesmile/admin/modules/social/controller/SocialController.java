package cn.mesmile.admin.modules.social.controller;

import cn.hutool.json.JSONUtil;
import cn.mesmile.admin.modules.social.config.SocialProperties;
import cn.mesmile.admin.modules.social.util.SocialAuthUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zb
 * @Description
 */
@Api(value = "第三方认证相关",tags = {"第三方认证相关api"})
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/oauth")
public class SocialController {

    private final AuthStateCache authStateCache;

    private final SocialProperties socialProperties;

    /**
     * 登录地址
     * @param type 第三方登录类型 {@link me.zhyd.oauth.config.AuthDefaultSource}
     */
    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = SocialAuthUtil.getAuthRequest(type, socialProperties, authStateCache);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 回调地址
     * @param type 第三方登录类型 {@link me.zhyd.oauth.config.AuthDefaultSource}
     * @param callback 回调携带参数
     */
    @GetMapping("/callback/{type}")
    public AuthResponse login(@PathVariable String type, AuthCallback callback) {
        AuthRequest authRequest = SocialAuthUtil.getAuthRequest(type, socialProperties, authStateCache);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", JSONUtil.toJsonStr(response));
        return response;
    }
}
