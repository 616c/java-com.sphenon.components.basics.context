package com.sphenon.basics.monitoring;

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/

import com.sphenon.basics.context.*;
import com.sphenon.basics.debug.*;

import java.io.PrintStream;

import java.util.Vector;

/**
   A {@link Problem} whose cause is an exception.
*/
public class ProblemException implements Problem, Dumpable, ContextAware {

    public ProblemException(CallContext context, Throwable exception) {
        this.context   = context;
        this.exception = exception;
    }

    protected CallContext context;

    public CallContext getContext () {
        return this.context;
    }

    public void setContext (CallContext context) {
        this.context = context;
    }

    protected Throwable exception;

    public Throwable getException (CallContext context) {
        return this.exception;
    }

    public void setException (CallContext context, Throwable exception) {
        this.exception = exception;
    }

    public String toString() {
        return (this.exception == null ? "" : this.exception.toString());
    }

    public String toString(CallContext context) {
        return (this.exception == null ? "" : dumpThrowable(context, this.exception));
    }

    static public int stack_trace_length = 10;

    static public String dumpThrowable(CallContext context, Throwable throwable) {
        String value = throwable.toString();
        StackTraceElement[] stacktrace = throwable.getStackTrace();
        for (int i=0; i<stacktrace.length && i < stack_trace_length; i++) {
            StackTraceElement ste = stacktrace[i];
            value += " [" + ste + "]";
        }
        if (throwable.getCause() != null) {
            value += " [CAUSE: " + dumpThrowable(context, throwable.getCause()) + " ]";
        }
        return value;
    }

    public void dump(CallContext context, DumpNode dump_node) {
        if (this.exception instanceof Dumpable) {
            ((Dumpable) this.exception).dump(context, dump_node);
        } else {
            dump_node.dump(context, "Exception", this.exception == null ? "null" : this.exception.toString());
            Vector<String> rss = RuntimeStep.getStack(this.getContext());
            if (rss != null) {
                DumpNode stack_dump_node = dump_node.openDump(context, "RuntimeSteps");
                for (String rs : rss) {
                    stack_dump_node.dump(context, rs);
                }
                stack_dump_node.close(context);
            }
            if (this.exception != null) {
                DumpNode stack_dump_node = dump_node.openDump(context, "StackTrace");
                for (StackTraceElement SE : exception.getStackTrace()) {
                    stack_dump_node.dump(context, SE.toString());
                }
                stack_dump_node.close(context);
                Throwable cause = this.exception.getCause();
                if (cause != null) {
                    dump_node.dump(context, "Cause", cause);
                }
            }
        }
    }
}
