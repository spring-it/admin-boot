package cn.mesmile.admin.modules.auth.security;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.filter.xss.WebUtil;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.AuthUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zb
 * @Description 退出登录成功处理类
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private TokenService tokenService;

    /**
     * 退出登录处理
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token = tokenService.getToken(request);
        if (StrUtil.isNotEmpty(token)){
            // 删除redis中的缓存
            tokenService.deleteToken(token);
        }
        WebUtil.renderString(response, JSONObject.toJSONString(R.success("退出登录成功")), HttpStatus.OK.value());
    }
}

