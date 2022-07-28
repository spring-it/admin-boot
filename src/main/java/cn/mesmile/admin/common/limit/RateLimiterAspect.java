package cn.mesmile.admin.common.limit;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.exceptions.RateLimiterException;
import cn.mesmile.admin.common.lock.AdminExpressionEvaluator;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.common.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description 限流拦截器
 */
@Slf4j
@Aspect
public class RateLimiterAspect implements ApplicationContextAware {

    private final AdminExpressionEvaluator EVALUATOR = new AdminExpressionEvaluator();
    private final RateLimiterClient rateLimiterClient;
    private ApplicationContext applicationContext;

    public RateLimiterAspect(RateLimiterClient rateLimiterClient) {
        this.rateLimiterClient = rateLimiterClient;
    }

    @Around("@annotation(rateLimiter)")
    public Object aroundRateLimiter(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        String prefix = rateLimiter.prefix();
        String param = rateLimiter.param();
        String customKey = rateLimiter.customKey();
        String keyName = prefix;
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (requestAttributes != null){
            HttpServletRequest request = requestAttributes.getRequest();
            String requestUri = request.getRequestURI();
            keyName = keyName + ":" + requestUri;
        }
        if (StrUtil.isNotBlank(param)) {
            String evalLockParam = EVALUATOR.evalLockParam(point, param, applicationContext);
            if (StrUtil.isNotBlank(evalLockParam)){
                keyName = keyName + ":" +evalLockParam ;
            }
        }
        if (StrUtil.isNotBlank(customKey)){
            keyName = keyName + ":" +customKey ;
        }
        if (LimiterModeEnum.LIMITER_IP.equals(rateLimiter.limiterMode())) {
            if (requestAttributes != null){
                HttpServletRequest request = requestAttributes.getRequest();
                String ipAddr = IpUtil.getIpAddr(request);
                // 将ip地址加入到键值中
                keyName = keyName +":"+ ipAddr;
            }
        }
        // 判断是否允许访问
        boolean allow = rateLimiterClient.isAllow(keyName, rateLimiter.max(), rateLimiter.ttl() , rateLimiter.timeUnit());
        if (allow){
            return point.proceed();
        }else {
            if (requestAttributes != null){
                HttpServletRequest request = requestAttributes.getRequest();
                // 请求路径
                String requestUrl = request.getRequestURL().toString();
                // 请求ip
                String ipAddr = IpUtil.getIpAddr(request);
                log.error("访问次数超限制：url:{} , ip:{}", requestUrl, ipAddr);
            }
            // 您的访问次数已超限：%s，速率：%d/%ds
            throw new RateLimiterException(ResultCode.FAILURE, rateLimiter.msg());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
