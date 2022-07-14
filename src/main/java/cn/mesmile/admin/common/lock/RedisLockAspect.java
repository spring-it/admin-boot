package cn.mesmile.admin.common.lock;

import cn.hutool.core.util.StrUtil;
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

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author zb
 * @Description
 */
@Aspect
public class RedisLockAspect implements ApplicationContextAware {

    private static final AdminExpressionEvaluator EVALUATOR = new AdminExpressionEvaluator();
    private final RedisLockClient redisLockClient;
    private ApplicationContext applicationContext;

    public RedisLockAspect(final RedisLockClient redisLockClient) {
        this.redisLockClient = redisLockClient;
    }

    @Around("@annotation(redisLock)")
    public Object aroundRedisLock(ProceedingJoinPoint point, RedisLock redisLock) {
        String lockName = redisLock.value();
        Assert.hasText(lockName, "@RedisLock 中 value 不能为 null 或为空");
        String prefix = redisLock.prefix();
        String lockParam = redisLock.param();
        String lockKey = prefix + ":" + lockName;
        if (StrUtil.isNotBlank(lockParam)) {
            String evalAsText = EVALUATOR.evalLockParam(point, lockParam, applicationContext);
            if (StrUtil.isNotBlank(evalAsText)){
                lockKey = lockKey + ':' + evalAsText;
            }
        }
        LockTypeEnum lockType = redisLock.type();
        long waitTime = redisLock.waitTime();
        long leaseTime = redisLock.leaseTime();
        TimeUnit timeUnit = redisLock.timeUnit();
        return this.redisLockClient.lock(lockKey, lockType, waitTime, leaseTime, timeUnit, point::proceed);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
