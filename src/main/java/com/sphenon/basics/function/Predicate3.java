package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Predicate3<T1,T2,T3> {
    boolean check(CallContext context, T1 argument1, T2 argument2, T3 argument3);
}
