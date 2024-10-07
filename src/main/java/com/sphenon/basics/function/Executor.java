package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Executor {
    void execute(CallContext context);
}
