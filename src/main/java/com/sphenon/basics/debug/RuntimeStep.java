package com.sphenon.basics.debug;

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

import java.util.Vector;
import java.util.List;
import java.util.LinkedList;

public class RuntimeStep {

    static public RuntimeStepFactory runtime_step_factory;

    static public String getStackDump(CallContext context) {
        return (runtime_step_factory == null ? "" : runtime_step_factory.getStackDump(context));
    }

    static public Vector<String> getStack(CallContext context) {
        return (runtime_step_factory == null ? null : runtime_step_factory.getStack(context));
    }

    static public List<List<RuntimeStep>> getRuntimeStepStack(CallContext context) {
        return (runtime_step_factory == null ? null : runtime_step_factory.getRuntimeStepStack(context));
    }

    public StackTraceElement[] getCreationStackTrace(CallContext context) {
        return null;
    }

    protected RuntimeStep(CallContext context) {
    }

    /**
      Instantiates a runtime step and normally registers this step via a
      {@link RuntimeStepManager} in the current context. Steps are registered
      in this context in sequence and kept until this internal history is
      cleared.

      Note: this method usually requires a local Context instance,
      otherwise the step stack will not be managed correctly.

      @param message Describes the purpose and goal of this step
      @param arguments Additional arguments describing the current step execution
      @return the Step instance
     */
    static public RuntimeStep create(Context context, long level, Class j_class, String message, Object... arguments) {
        return (runtime_step_factory != null ? runtime_step_factory.createStep(context, level, j_class, message, arguments) : new RuntimeStep(context));
    }

    /**
       Marks this step as completed.

       @param message Additional information about completion
     */
    public void setCompleted(CallContext context, String message, Object... arguments) {
    }

    /**
       Convenience method.
    */
    public void setCompleted(CallContext context) {
        this.setCompleted(context, null);
    }

    /**
       Marks this step as failed.

       @param message Additional information about failure
     */
    public void setFailed(CallContext context, Throwable exception, String message, Object... arguments) {
        this.exception = exception;
    }

    /**
       Convenience method.
    */
    public void setFailed(CallContext context) {
        this.setFailed(context, (Throwable) null, (String) null);
    }

    /**
       Convenience method.
    */
    public void setFailed(CallContext context, Throwable exception) {
        this.setFailed(context, exception, (String) null);
    }

    /**
       Convenience method.
    */
    public void setFailed(CallContext context, String message, Object... arguments) {
        this.setFailed(context, (Throwable) null, message, arguments);
    }

    protected Throwable exception;

    public Throwable getException (CallContext context) {
        return this.exception;
    }

    /*
      need to move basic interfaces to context package

    public void setExecution(CallContext context, ...
    public void setProgress(CallContext context, ...

    was man hiermit noch tun kann
     - performance analysieren (timer einbauen)
     - step-abhängig automatisch notifications einbauen, etwa
       'in step "xyz.*" bitte a.b.c.DIAGNOSTICS'

    */
}
