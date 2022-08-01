package cn.mesmile.admin.modules.auth.controller;

import cn.hutool.core.util.IdUtil;
import cn.mesmile.admin.common.captcha.CaptchaProperties;
import cn.mesmile.admin.common.captcha.CaptchaUtil;
import cn.mesmile.admin.common.captcha.CaptchaVO;
import cn.mesmile.admin.common.limit.LimiterModeEnum;
import cn.mesmile.admin.common.limit.RateLimiter;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.modules.auth.domain.request.LoginRequest;
import cn.mesmile.admin.modules.auth.domain.vo.LoginVO;
import cn.mesmile.admin.modules.auth.service.ILoginService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
@Api(value = "登陆退出相关api", tags = {"登陆退出相关api"})
@Validated
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    private final Producer producer;

    private final AdminRedisTemplate adminRedisTemplate;

    private final CaptchaProperties captchaProperties;


    @PostMapping("/login")
    @ApiOperation("登录接口")
    public R<LoginVO> login(@RequestBody @Validated LoginRequest loginRequest) {
        LoginVO login = loginService.login(loginRequest);
        return R.data(login);
    }

    /**
     *  两种方式退出登录
     * 方式一：{http://ip:port/user/logout}
     * 登录成功拦截器
     * 方式二：{http://ip:port/logout}{@link cn.mesmile.admin.modules.auth.security.LogoutSuccessHandlerImpl}
     *
     * @return 处理结果
     */
    @PostMapping("/user/logout")
    public R logout(){
        boolean result = loginService.logout();
        return R.status(result);
    }

    /** 限制频率在 5 秒钟 3次 */
    @RateLimiter(limiterMode = LimiterModeEnum.LIMITER_IP,max = 3, ttl = 6,timeUnit = TimeUnit.SECONDS)
    @ApiOperation("获取验证码")
    @GetMapping("/captcha")
    public R<CaptchaVO> getCaptcha(){
        String uuid = IdUtil.fastSimpleUUID();
        String imageBase64Str = CaptchaUtil.getImageBase64Str(producer, adminRedisTemplate, captchaProperties, uuid);
        CaptchaVO captchaVO = new CaptchaVO(uuid, imageBase64Str);
        return R.data(captchaVO);
    }

}
