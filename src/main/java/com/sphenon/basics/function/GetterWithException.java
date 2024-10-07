package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface GetterWithException<T, E extends Throwable> {
    T get(CallContext context) throws E;
}
