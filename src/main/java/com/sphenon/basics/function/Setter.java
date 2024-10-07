package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Setter<T> {
    void set(CallContext context, T t);
}
