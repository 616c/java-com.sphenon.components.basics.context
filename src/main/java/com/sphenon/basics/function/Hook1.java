package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Hook1<T> {
    void callback(CallContext context, T argument1);
}
