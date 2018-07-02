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

public class ExecutionContext extends SpecificContext {

    static public ExecutionContext getOrCreate(Context context) {
        ExecutionContext execution_context = (ExecutionContext) context.getSpecificContext(ExecutionContext.class);
        if (execution_context == null) {
            execution_context = new ExecutionContext(context);
            context.setSpecificContext(ExecutionContext.class, execution_context);
        }
        return execution_context;
    }

    static public ExecutionContext get(Context context) {
        ExecutionContext execution_context = (ExecutionContext) context.getSpecificContext(ExecutionContext.class);
        return execution_context;
    }

    static public ExecutionContext create(Context context) {
        ExecutionContext execution_context = new ExecutionContext(context);
        context.setSpecificContext(ExecutionContext.class, execution_context);
        return execution_context;
    }

    protected ExecutionContext (Context context) {
        super(context);
    }

    protected ExecutionInterceptor execution_interceptor;

    public void setExecutionInterceptor(CallContext cc, ExecutionInterceptor execution_interceptor) {
        this.execution_interceptor = execution_interceptor;
    }

    public ExecutionInterceptor getExecutionInterceptor(CallContext cc) {
        ExecutionContext ec;
        return (this.execution_interceptor != null ?
                     this.execution_interceptor
                  : (ec = (ExecutionContext) this.getCallContext(ExecutionContext.class)) != null ?
                       ec.getExecutionInterceptor(cc)
                     : null
               );
    }

    static public boolean notifyInterceptor(CallContext context, ExecutionControl execution_control) {
        ExecutionContext execution_context = ExecutionContext.get((Context) context);
        if (execution_context == null) { return false; }
        ExecutionInterceptor execution_interceptor = execution_context.getExecutionInterceptor(context);
        if (execution_interceptor == null) { return false; }
        execution_interceptor.notify(context, execution_control);
        return true;
    }
}
