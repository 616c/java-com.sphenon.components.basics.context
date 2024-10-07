package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

public class MergerAND implements Merger<Boolean> {
    public Boolean merge(CallContext context, Boolean b1, Boolean b2) {
        return (    (b1 != null && b1 == true)
                 && (b2 != null && b2 == true)
               ) ? true : false;
    }
}
