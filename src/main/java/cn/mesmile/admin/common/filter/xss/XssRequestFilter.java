package cn.mesmile.admin.common.filter.xss;

import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/***
 * 自定义xss过滤器
 * @author zb
 * @Description
 */
public class XssRequestFilter implements Filter {

    private final XssProperties xssProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getServletPath();
        if (this.xssProperties.getEnabled() && !this.isXssSkip(path)) {
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(xssRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 判断是否跳过路径
     * @param path
     * @return
     */
    private boolean isXssSkip(String path) {
        return this.xssProperties.getSkipUrl().stream().anyMatch((pattern) -> {
            return this.antPathMatcher.match(pattern, path);
        });
    }

    @Override
    public void destroy() {
    }

    public XssRequestFilter(final XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }
}