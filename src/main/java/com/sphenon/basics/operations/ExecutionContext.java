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
    static final public Class _class = ExecutionContext.class;

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

    protected Boolean active_block;
    protected String  block_id;

    public void beginActiveBlock(CallContext context, String block_id) {
        if (this.active_block != null || this.block_id != null) {
            throw new Error("Inconsistency: an active block can only be started once");
        }
        this.block_id     = block_id;
        this.active_block = true;
    }

    public void cancelActiveBlock(CallContext context, String block_id) {
        if (this.active_block != null) {
            this.active_block = false;
        }
        if (    block_id != null
             && block_id.equals(this.block_id) == false
           ) {
            ExecutionContext ec = (ExecutionContext) this.getCallContext(ExecutionContext.class);
            if (ec == null) {
                throw new Error("Inconsistency: could not find active block with id '" + block_id + "' to be cancelled");
            }
            ec.cancelActiveBlock(context, block_id);
        }
    }

    public void finishActiveBlock(CallContext context, String block_id) {
        if (this.active_block == null) {
            throw new Error("Inconsistency: an active block can only be finished in the same context where it was started, and it can be finished only once");
        }
        if (block_id == null ?
               this.block_id != null
             : block_id.equals(this.block_id) == false
           ) {
            throw new Error("Inconsistency: an active block can only be finished with the same block id as it was started with ('" + this.block_id + "', '" + block_id + "')");
        }
        this.active_block = null;
        this.block_id     = null;
    }

    public boolean getBlockIsActive(CallContext context) {
        ExecutionContext ec;
        return (this.active_block != null ?
                     this.active_block
                  : (ec = (ExecutionContext) this.getCallContext(ExecutionContext.class)) != null ?
                       ec.getBlockIsActive(context)
                     : true
               );
    }
}
