package cn.mesmile.admin.common.lock;

import org.springframework.lang.Nullable;


/**
 * @author zb
 * @Description
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    @Nullable
    T get() throws Throwable;
}