package com.sphenon.basics.function;

import com.sphenon.basics.context.*;
import com.sphenon.basics.operations.*;

@FunctionalInterface
public interface ExecutorWithResult {
    Execution execute(CallContext context);
}
