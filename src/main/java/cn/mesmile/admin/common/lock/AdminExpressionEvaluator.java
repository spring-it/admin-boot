package cn.mesmile.admin.common.lock;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zb
 * @Description spring el 表达式解析
 */
public class AdminExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap(64);
    private final Map<AnnotatedElementKey, Method> methodCache = new ConcurrentHashMap(64);

    public EvaluationContext createContext(Method method, Object[] args, Object target, Class<?> targetClass, @Nullable BeanFactory beanFactory) {
        Method targetMethod = this.getTargetMethod(targetClass, method);
        AdminExpressionRootObject rootObject = new AdminExpressionRootObject(method, args, target, targetClass, targetMethod);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, targetMethod, args, this.getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    public EvaluationContext createContext(Method method, Object[] args, Class<?> targetClass, Object rootObject, @Nullable BeanFactory beanFactory) {
        Method targetMethod = this.getTargetMethod(targetClass, method);
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, targetMethod, args, this.getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    /**
     * 解析el表达式
     */
    public String evalLockParam(HttpServletRequest request,Method method, String lockParam, ApplicationContext applicationContext) {
        //        MethodSignature ms = (MethodSignature)point.getSignature();
//        Object[] args = point.getArgs();
//        Object target = point.getTarget();
//        Class<?> targetClass = target.getClass();
//        EvaluationContext context = createContext(method, args, target, targetClass, applicationContext);
//        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
//        // 判断是否有多个表达式
//        List<String> splitValue = StrUtil.split(lockParam, ";");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String param : splitValue) {
//            if (StrUtil.isNotBlank(param)){
//                String value = evalAsText(param, elementKey, context);
//                stringBuilder.append(value);
//            }
//        }
//        return stringBuilder.toString();
        return "";
    }

    /**
     * 解析el表达式
     */
    public String evalLockParam(ProceedingJoinPoint point, String lockParam, ApplicationContext applicationContext) {
        MethodSignature ms = (MethodSignature)point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        Object target = point.getTarget();
        Class<?> targetClass = target.getClass();
        EvaluationContext context = createContext(method, args, target, targetClass, applicationContext);
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        // 判断是否有多个表达式
        List<String> splitValue = StrUtil.split(lockParam, ";");
        StringBuilder stringBuilder = new StringBuilder();
        for (String param : splitValue) {
            if (StrUtil.isNotBlank(param)){
                String value = evalAsText(param, elementKey, context);
                stringBuilder.append(value);
            }
        }
        return stringBuilder.toString();
    }

    @Nullable
    public Object eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return this.eval(expression, methodKey, evalContext, (Class)null);
    }

    @Nullable
    public <T> T eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext, @Nullable Class<T> valueType) {
        return this.getExpression(this.expressionCache, methodKey, expression).getValue(evalContext, valueType);
    }

    @Nullable
    public String evalAsText(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return (String)this.eval(expression, methodKey, evalContext, String.class);
    }

    public boolean evalAsBool(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return Boolean.TRUE.equals(this.eval(expression, methodKey, evalContext, Boolean.class));
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        return (Method)this.methodCache.computeIfAbsent(methodKey, (key) -> {
            return AopUtils.getMostSpecificMethod(method, targetClass);
        });
    }

    public void clear() {
        this.expressionCache.clear();
        this.methodCache.clear();
    }
}
