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
import com.sphenon.basics.debug.*;
import com.sphenon.basics.monitoring.*;

import com.sphenon.basics.operations.*;

public class Class_BreakpointAfterActivity implements ExecutionBreakpoint {

    protected ExecutionControl execution_control;

    public Class_BreakpointAfterActivity (CallContext context, ExecutionControl execution_control) {
        this.execution_control = execution_control;
    }

    public boolean matches(CallContext context, ExecutionControl execution_control) {
        return (    this.execution_control == execution_control
                 && execution_control.getPoint(context) == ExecutionControl.Point.AfterActivity
               );
    }

    public boolean getClearAfterMatch(CallContext context) {
        return true;
    }
}
