package com.sphenon.basics.function;

import com.sphenon.basics.context.*;

@FunctionalInterface
public interface Converter<SourceType,TargetType> {
    TargetType convert(CallContext context, SourceType source);
}
