package com.sun.cache;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @Author XHT
 * @Date 2019/12/8 18:52
 * @Desc
 **/
public class InheritThreadLocal<T> extends InheritableThreadLocal<T> {

    /**
     * 创建一个子线程自动继承的自动初始化的 ThreadLocal
     *
     * @param <S>      类型
     * @param supplier 初始元素工厂
     * @return 子线程自动继承的自动初始化的 ThreadLocal
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedInheritThreadLocal<>(supplier);
    }

    /**
     * 子线程自动继承的自动初始化的 ThreadLocal
     *
     * @param <T> 类型
     */
    static final class SuppliedInheritThreadLocal<T> extends InheritThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedInheritThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return this.supplier.get();
        }
    }
}
