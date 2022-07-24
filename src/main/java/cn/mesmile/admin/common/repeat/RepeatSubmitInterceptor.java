package cn.mesmile.admin.common.repeat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.mesmile.admin.common.exceptions.RepeatSubmitException;
import cn.mesmile.admin.common.lock.AdminExpressionEvaluator;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zb
 * @Description
 */
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor, ApplicationContextAware {

    private final AdminRedisTemplate adminRedisTemplate;

    private ApplicationContext applicationContext;

    public RepeatSubmitInterceptor(AdminRedisTemplate adminRedisTemplate) {
        this.adminRedisTemplate = adminRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation, handlerMethod)) {
                    throw new RepeatSubmitException(ResultCode.FAILURE, annotation.msg());
                }
            }
        }
        return true;
    }

    private static final AdminExpressionEvaluator EVALUATOR = new AdminExpressionEvaluator();

    /**
     * 是否是 重复提交
     * @param request 请求体
     * @param annotation 注解
     * @param handlerMethod 拦截方法
     * @return
     */
    private boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        List<Object> objects = new ArrayList<>();
        String jsonValue = null;
        Map<String, String[]> parameterMap = null;
        if (request instanceof RepeatSubmitRequestWrapper) {
            RepeatSubmitRequestWrapper requestWrapper = (RepeatSubmitRequestWrapper) request;
            jsonValue = requestWrapper.getRequestBody();
            Parameter[] parameters = method.getParameters();
            if (parameters != null && parameters.length > 0 && StrUtil.isNotEmpty(jsonValue)) {
                for (Parameter parameter : parameters) {
                    Class<?> type = parameter.getType();
                    Object object = JSONObject.parseObject(jsonValue, type);
                    objects.add(object);
                }
            }
        } else {
            parameterMap = request.getParameterMap();
            if (parameterMap != null && parameterMap.size() > 0) {
                Collection<String[]> values = parameterMap.values();
                objects.addAll(values);
            }
        }
        String param = annotation.param();
        String value = "";
        // 提取自定义参数
        if (StrUtil.isNotBlank(param)) {
            value = EVALUATOR.evalLockParam(method, objects.toArray(), handlerMethod.getBean(), param, applicationContext);
        } else {
            if (request instanceof RepeatSubmitRequestWrapper) {
                // 提取json数据
                value = jsonValue;
            } else {
                // 读取 get 请求数据
                if (parameterMap != null && parameterMap.size() > 0) {
                    String parameters = JSONObject.toJSONString(parameterMap);
                    value = value + parameters;
                }
            }
        }
        String uri = request.getRequestURI();
        String key = annotation.prefix() + ":" + uri;
        if (StrUtil.isNotBlank(value)) {
            String md5Str = MD5.create().digestHex(value, StandardCharsets.UTF_8);
            key = key + ":" + md5Str;
        }
        boolean result = adminRedisTemplate.setIfAbsentExpire(key, value, annotation.interval(), annotation.timeUnit());
        if (!result) {
            log.error("重复提交异常：uri:{}, param:{}", uri, value);
        }
        return !result;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
