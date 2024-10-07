package com.sphenon.basics.testing;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.operations.*;

public class TestResult_Execution implements TestResult {

    protected ProblemState problem_state;
    protected Problem problem;
    protected String string;

    public TestResult_Execution (CallContext context, Execution execution) {
        this.execution = execution;

        java.lang.Runtime.getRuntime().gc();

        long total = java.lang.Runtime.getRuntime().totalMemory();
        long free  = java.lang.Runtime.getRuntime().freeMemory();
        System.err.println("Before TestResult: " + total + " - " + free + " - " + (total - free));

        this.problem_state = this.execution.getProblemState(context);
        this.problem = this.execution.getProblem(context);
        this.string = this.getProblemState(context).toString().toUpperCase() + (this.getProblem(context) == null ? "" : this.getProblem(context).toString());

        System.err.println("STORED STRING IN TestResult: " + string.length());

        this.execution = null;

        java.lang.Runtime.getRuntime().gc();

        total = java.lang.Runtime.getRuntime().totalMemory();
        free  = java.lang.Runtime.getRuntime().freeMemory();
        System.err.println("After TestResult: " + total + " - " + free + " - " + (total - free));
    }

    protected Execution execution;

    public Execution getExecution (CallContext context) {
        System.err.println("REQUESTED EXECUTION FROM TestResult");
        return this.execution;
    }

    public ProblemState getProblemState (CallContext context) {
        return this.problem_state;
    }

    public Problem getProblem (CallContext context) {
        return this.problem;
    }

    public String toString() {
        return this.string;
    }
}
