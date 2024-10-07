package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Predicate2<T1,T2> {
    boolean check(CallContext context, T1 argument1, T2 argument2);
}
