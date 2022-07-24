package cn.mesmile.admin.modules.auth.service;

import cn.mesmile.admin.modules.auth.domain.request.LoginRequest;
import cn.mesmile.admin.modules.auth.domain.vo.LoginVO;

/**
 * @author zb
 * @Description
 */
public interface ILoginService {

    /**
     * 登录接口
     * @param loginRequest 登录请求
     * @return 登录成功
     */
    LoginVO login(LoginRequest loginRequest);

    /**
     * 退出登录
     * @return 是否退出成功
     */
    boolean logout();
}
