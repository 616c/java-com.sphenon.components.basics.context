package com.sphenon.basics.operations.classes;

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
import com.sphenon.basics.system.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.operations.*;

import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Vector;

public class Class_ExecutionInterceptor implements ExecutionInterceptor {

    public Class_ExecutionInterceptor(CallContext context) {
    }

    protected boolean step_mode;

    public boolean getStepMode (CallContext context) {
        return this.step_mode;
    }

    public void disableStepMode (CallContext context) {
        this.step_mode = false;
    }

    public void enableStepMode (CallContext context) {
        this.step_mode = true;
    }

    protected boolean trace_mode;

    public boolean getTraceMode (CallContext context) {
        return this.trace_mode;
    }

    public void disableTraceMode (CallContext context) {
        this.trace_mode = false;
    }

    public void enableTraceMode (CallContext context) {
        this.trace_mode = true;
    }

    protected Vector<ExecutionBreakpoint> breakpoints;

    public Vector<ExecutionBreakpoint> getBreakpoints (CallContext context) {
        return this.breakpoints;
    }

    public void addBreakpoint (CallContext context, ExecutionBreakpoint breakpoint) {
        if (this.breakpoints == null) {
            this.breakpoints = new Vector<ExecutionBreakpoint>();
        }
        this.breakpoints.add(breakpoint);
    }

    public void clearBreakpoint (CallContext context, ExecutionBreakpoint breakpoint) {
        if (this.breakpoints == null) { return; }
        this.breakpoints.remove(breakpoint);
    }

    protected boolean breakHere(CallContext context, ExecutionControl execution_control) {
        if (this.breakpoints != null) {
            for (ExecutionBreakpoint breakpoint : this.breakpoints) {
                if (breakpoint.matches(context, execution_control)) {
                    if (breakpoint.getClearAfterMatch(context)) {
                        this.clearBreakpoint(context, breakpoint);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void notify(CallContext context, ExecutionControl execution_control) {
        SystemContext sc = SystemContext.getOrCreate((Context) context);

        if (this.getStepMode(context) == false) {
            if (this.breakHere(context, execution_control)) {
                this.enableStepMode(context);
            } else {
                if (this.getTraceMode(context) == true) {
                    switch(execution_control.getPoint(context)) {
                        case BeforeActivity: sc.getOutputStream(context).print("[executing] "); break;                            
                        case AfterActivity : sc.getOutputStream(context).print("[done] "); break;                            
                        case BeforeFork    : sc.getOutputStream(context).print("[forking] "); break;                            
                    }
                    sc.getOutputStream(context).println(execution_control.getShortDescription(context));
                    sc.getOutputStream(context).flush();
                }
                return;
            }
        }
        Vector<ContinuationOption> options = execution_control.getContinuationOptions(context);
        if (options == null || options.size() == 0) {
            return;
        }
        InputStreamReader isr = null;
        try {
            sc.getOutputStream(context).println("------------------------------------------------------------------------");
            sc.getOutputStream(context).println("   " + execution_control.getShortDescription(context));
            sc.getOutputStream(context).flush();
            StringBuilder key_string = new StringBuilder();
            StringBuilder help_string = new StringBuilder();
            boolean first = true;
            key_string.append("[");
            for (ContinuationOption option : options) {
                if (first) { first = false; } else { help_string.append(", "); }
                key_string.append(option.getKeys(context));
                help_string.append(option.getDescription(context));
                help_string.append(" [");
                help_string.append(option.getKeys(context));
                help_string.append("]");
            }
            key_string.append("?] ");

            isr  = new InputStreamReader(sc.getInputStream(context));
            boolean valid = false;
            do {            
                sc.getOutputStream(context).print(key_string.toString());
                sc.getOutputStream(context).flush();
                int c = isr.read();
                if (c != -1) {
                    for (ContinuationOption option : options) {
                        if (option.matchesKey(context, (char) c)) {
                            execution_control.selectContinuation(context, option, this);
                            valid =  true;
                            break;
                        }
                    }
                    if ( ! valid) {
                        if (((char) c) != '?') {
                            sc.getOutputStream(context).println("invalid input: " + c);
                        }
                        sc.getOutputStream(context).println(help_string.toString());
                        sc.getOutputStream(context).flush();
                    }
                }
            } while ( ! valid);
        } catch (IOException ioe) {
            sc.getErrorStream(context).println("Cannot read from terminal");
            sc.getErrorStream(context).flush();
        }
    }
}
