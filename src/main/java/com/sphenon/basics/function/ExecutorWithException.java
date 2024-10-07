package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface ExecutorWithException<E extends Throwable> {
    void execute(CallContext context) throws E;
}
