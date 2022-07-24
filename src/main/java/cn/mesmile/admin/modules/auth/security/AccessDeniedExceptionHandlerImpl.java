package cn.mesmile.admin.modules.auth.security;

import cn.mesmile.admin.common.filter.xss.WebUtil;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.result.ResultCode;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zb
 * @Description 自定义[授权]处理器 403
 */
@Component
public class AccessDeniedExceptionHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        R<Object> r = R.fail(ResultCode.REQ_REJECT, "权限不足");
        String result = JSONObject.toJSONString(r);
        WebUtil.renderString(response, result, HttpStatus.FORBIDDEN.value());
    }

}