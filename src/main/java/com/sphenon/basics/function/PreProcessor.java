package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface PreProcessor<Type> {
    Type preprocess(CallContext context, Type value);
}
