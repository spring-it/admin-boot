package cn.mesmile.admin.common.lock;

import org.springframework.lang.Nullable;


/**
 * @author zb
 * @Description 可以抛出异常的
 */
@FunctionalInterface
public interface ThrowSupplier<T> {

    @Nullable
    T get() throws Throwable;
}