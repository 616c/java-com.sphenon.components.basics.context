package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Getter<T> {
    T get(CallContext context);
}
