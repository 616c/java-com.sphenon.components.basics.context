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
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;

import com.sphenon.basics.operations.*;

import java.util.Vector;

public class Class_ExecutionControl implements ExecutionControl {

    public Class_ExecutionControl(CallContext context) {
    }

    protected String short_description;

    public String getShortDescription (CallContext context) {
        return this.short_description;
    }

    public void setShortDescription (CallContext context, String short_description) {
        this.short_description = short_description;
    }


    protected Point point;

    public Point getPoint (CallContext context) {
        return this.point;
    }

    public void setPoint (CallContext context, Point point) {
        this.point = point;
    }

    static protected Vector<ContinuationOption> standard_continuation_options;

    protected Vector<ContinuationOption> continuation_options;

    public Vector<ContinuationOption> getContinuationOptions(CallContext context) {
        if (this.point != Point.BeforeActivity) {
            return null;
        }
        if (this.continuation_options == null) {
            if (standard_continuation_options == null) {
                standard_continuation_options = new Vector<ContinuationOption>();
                standard_continuation_options.add(ContinuationOption.STEP_OVER);
                standard_continuation_options.add(ContinuationOption.STEP_INTO);
                standard_continuation_options.add(ContinuationOption.SKIP_STEP);
                standard_continuation_options.add(ContinuationOption.PROCEED);
                standard_continuation_options.add(ContinuationOption.CANCEL);
            }
            this.continuation_options = standard_continuation_options;
        }
        return this.continuation_options;
    }

    protected ContinuationOption option;

    public ContinuationOption getContinuation(CallContext context) {
        return this.option;
    }

    public void selectContinuation(CallContext context, ContinuationOption option, ExecutionInterceptor interceptor) {
        this.option = option;

        if (this.option == ContinuationOption.STEP_OVER) {
            interceptor.disableStepMode(context);
            interceptor.addBreakpoint(context, new Class_BreakpointAfterActivity(context, this));
        } else if (this.option == ContinuationOption.PROCEED) {
            interceptor.disableStepMode(context);
        }
    }
}
