package com.sphenon.basics.function;

import com.sphenon.basics.context.*;
import com.sphenon.basics.event.*;

@FunctionalInterface
public interface Listener {
    void notify(CallContext context, Event e);
}
