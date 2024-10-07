package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Predicate1<T1> {
    boolean check(CallContext context, T1 argument1);
}
