package cn.mesmile.admin.common.lock;

import lombok.Data;

import java.lang.reflect.Method;


/**
 * @author zb
 * @Description
 */
@Data
public class AdminExpressionRootObject {

    private  Method method;
    private  Object[] args;
    private  Object target;
    private  Class<?> targetClass;
    private  Method targetMethod;

    public AdminExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod) {
        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
    }
}
