package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Creator<T> {
    T create(CallContext context);
}
