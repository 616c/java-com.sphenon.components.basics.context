package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Comparator<Type> {
    int compare(CallContext context, Type value1, Type value2);
}
