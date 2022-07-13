package cn.mesmile.admin.common.repeat;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zb
 * @Description 包装可重复读的 inputStream
 */
public class RepeatSubmitFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest
                // 只拦截 contentType 为 "application/json" 的请求头 做处理，可重复读
                && StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            ServletRequest requestWrapper = new RepeatSubmitRequestWrapper((HttpServletRequest) request, response);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}