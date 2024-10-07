package com.sphenon.basics.function;

import com.sphenon.basics.context.*;
import com.sphenon.basics.operations.*;
import com.sphenon.basics.data.*;

@FunctionalInterface
public interface ExecutorWithProgress {
    void execute(CallContext context, DataSink<Execution> execution_sink);
}
