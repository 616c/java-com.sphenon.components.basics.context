package com.sphenon.basics.operations;

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
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;

import java.util.Vector;

public interface ExecutionInterceptor {
    public void notify(CallContext context, ExecutionControl execution_control);

    public boolean getStepMode(CallContext context);
    public void disableStepMode(CallContext context);
    public void enableStepMode(CallContext context);

    public boolean getTraceMode(CallContext context);
    public void disableTraceMode(CallContext context);
    public void enableTraceMode(CallContext context);

    public Vector<ExecutionBreakpoint> getBreakpoints (CallContext context);
    public void addBreakpoint (CallContext context, ExecutionBreakpoint breakpoint);
    public void clearBreakpoint (CallContext context, ExecutionBreakpoint breakpoint);
}
