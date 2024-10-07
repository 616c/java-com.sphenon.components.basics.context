package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Registry<T> {
    T get(CallContext context, String id);
}
