package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Merger<T> {
    T merge(CallContext context, T t1, T t2);
}
