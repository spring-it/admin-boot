package cn.mesmile.admin.common.filter.space;

import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/***
 * 自定义space过滤器
 * @author zb
 */
public class SpaceRequestFilter implements Filter {

    private final SpaceProperties spaceProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getServletPath();
        if (this.spaceProperties.getEnabled() && !this.isSpaceSkip(path)) {
            SpaceHttpServletRequestWrapper spaceRequest = new SpaceHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(spaceRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isSpaceSkip(String path) {
        return this.spaceProperties.getSkipUrl().stream().anyMatch((pattern) -> {
            return this.antPathMatcher.match(pattern, path);
        });
    }

    @Override
    public void destroy() {
    }

    public SpaceRequestFilter(final SpaceProperties spaceProperties) {
        this.spaceProperties = spaceProperties;
    }
}