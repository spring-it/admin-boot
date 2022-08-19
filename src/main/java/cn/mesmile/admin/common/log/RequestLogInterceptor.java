package cn.mesmile.admin.common.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zb
 * @Description
 */
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        // 生成RequestId
        String requestId = this.getRequestId();
        // 配置日志文件打印 REQUEST_ID
        MDC.put("REQUEST_ID", requestId);

        log.info("servletPath:{}", servletPath);
        log.info("preHandle 前置处理----------");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("preHandle 处理中----------");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String servletPath = request.getServletPath();

        log.info("preHandle 后置处理----------");
        log.info("servletPath:{}", servletPath);
        RequestIdUtils.removeRequestId();
        MDC.clear();
    }

    /**
     * 获取RequestId
     * 优先从header头获取，如果没有则自己生成
     * @return RequestId
     */
    private String getRequestId(){
        // 因为如果有网关，则一般会从网关传递过来，所以优先从header头获取
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null && StringUtils.hasText(attributes.getRequest().getHeader("x-request-id"))) {
            HttpServletRequest request = attributes.getRequest();
            String requestId = request.getHeader("x-request-id");
            UUID uuid = UUID.fromString(requestId);
            RequestIdUtils.generateRequestId(uuid);
            return requestId;
        }
        UUID existUUID = RequestIdUtils.getRequestId();
        if(existUUID != null){
            return existUUID.toString();
        }
        RequestIdUtils.generateRequestId();
        return RequestIdUtils.getRequestId().toString();
    }
}
