package cn.mesmile.admin.common.lock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import jodd.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;

/**
 * @author zb
 * @Description
 */
@Aspect
public class RedisLockAspect implements ApplicationContextAware {

    private static final AdminExpressionEvaluator EVALUATOR = new AdminExpressionEvaluator();
    private final RedisLockClient redisLockClient;
    private ApplicationContext applicationContext;

    @Around("@annotation(redisLock)")
    public Object aroundRedisLock(ProceedingJoinPoint point, RedisLock redisLock) {
        String lockName = redisLock.value();
        Assert.hasText(lockName, "@RedisLock value must have length; it must not be null or empty");
        String lockParam = redisLock.param();
        String lockKey;
        if (StringUtil.isNotBlank(lockParam)) {
            String evalAsText = this.evalLockParam(point, lockParam);
            lockKey = lockName + ':' + evalAsText;
        } else {
            lockKey = lockName;
        }

        LockTypeEnum lockType = redisLock.type();
        long waitTime = redisLock.waitTime();
        long leaseTime = redisLock.leaseTime();
        TimeUnit timeUnit = redisLock.timeUnit();
        RedisLockClient redisLockClient = this.redisLockClient;
        point.getClass();
        return redisLockClient.lock(lockKey, lockType, waitTime, leaseTime, timeUnit, point::proceed);
    }

    private String evalLockParam(ProceedingJoinPoint point, String lockParam) {
        MethodSignature ms = (MethodSignature)point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        Object target = point.getTarget();
        Class<?> targetClass = target.getClass();
        EvaluationContext context = EVALUATOR.createContext(method, args, target, targetClass, this.applicationContext);
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        return EVALUATOR.evalAsText(lockParam, elementKey, context);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public RedisLockAspect(final RedisLockClient redisLockClient) {
        this.redisLockClient = redisLockClient;
    }
}
