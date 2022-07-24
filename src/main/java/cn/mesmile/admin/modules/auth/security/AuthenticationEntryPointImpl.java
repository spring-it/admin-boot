package cn.mesmile.admin.modules.auth.security;

import cn.mesmile.admin.common.filter.xss.WebUtil;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.result.ResultCode;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zb
 * @Description 自定义[认证]处理器 401
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        R<Object> r = R.fail(ResultCode.UN_AUTHORIZED, "请求未认证");
        String result = JSONObject.toJSONString(r);
        WebUtil.renderString(response, result, HttpStatus.UNAUTHORIZED.value());
    }

}