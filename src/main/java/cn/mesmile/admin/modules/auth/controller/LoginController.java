package cn.mesmile.admin.modules.auth.controller;

import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.modules.auth.domain.request.LoginRequest;
import cn.mesmile.admin.modules.auth.domain.vo.LoginVO;
import cn.mesmile.admin.modules.auth.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public R<LoginVO> login(@RequestBody LoginRequest loginRequest) {
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

}
