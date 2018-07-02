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
import com.sphenon.basics.processing.*;

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;

public class Execution_Basic implements Execution, Dumpable {

    // static protected long all      = 0;

    public Execution_Basic (CallContext context) {
        // all++;
        // System.err.println("Basic: " + all);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = progression;
        this.record         = record;
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = progression;
        this.record         = null;
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = null;
        this.record         = null;
    }

    static public Execution_Basic createExecutionSuccess(CallContext context) {
        return new Execution_Basic (context, null, ProblemState.OK, (Problem) null, ActivityState.COMPLETED);
    }

//     static public Execution_Basic createExecutionSuccess(CallContext context, String instruction_description) {
//         return new Execution_Basic (context, new Class_Instruction(context, instruction_description), ProblemState.OK, (Problem) null, ActivityState.COMPLETED);
//     }

    static public Execution_Basic createExecutionFailure(CallContext context, Throwable exception) {
        return new Execution_Basic (context, null, ProblemState.ERROR, new ProblemException(context, exception), ActivityState.ABORTED);
    }

    static public Execution_Basic createExecutionFailure(CallContext context, ProblemState problem_state, Problem problem) {
        return new Execution_Basic (context, null, problem_state, problem, ActivityState.ABORTED);
    }

    static public Execution_Basic createExecutionFailure(CallContext context, String problem_description) {
        return new Execution_Basic (context, null, ProblemState.ERROR, new ProblemMessage(context, problem_description), ActivityState.ABORTED);
    }

//     static public Execution_Basic createExecutionFailure(CallContext context, String instruction_description, Throwable exception) {
//         return new Execution_Basic (context, new Class_Instruction(context, instruction_description), ProblemState.ERROR, new ProblemException(context, exception), ActivityState.ABORTED);
//     }

//     static public Execution_Basic createExecutionSkipped(CallContext context) {
//         return new Execution_Basic (context, null, ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.UNREADY, Class_Progression.NO_PROGRESS);
//     }

//     static public Execution_Basic createExecutionSkipped(CallContext context, String instruction_description) {
//         return new Execution_Basic (context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.UNREADY, Class_Progression.NO_PROGRESS);
//     }

//     static public Execution_Basic createExecutionInProgress(CallContext context) {
//         return new Execution_Basic (context, null, ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS);
//     }

//     static public Execution_Basic createExecutionInProgress(CallContext context, String instruction_description) {
//         return new Execution_Basic (context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS);
//     }

    static public Execution_Basic createExecution(CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return new Execution_Basic(context, instruction, problem_state, problem, activity_state, progression, record);
    }

//     static public Execution_Basic createExecution(CallContext context, String instruction_description, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
//         return new Execution_Basic(context, new Class_Instruction(context, instruction_description), problem_state, problem, activity_state, progression, record);
//     }

    public void setSuccess(CallContext context) {
        this.setProblemState(context, ProblemState.OK);
        this.setProblem(context, null);
        this.setActivityState(context, ActivityState.COMPLETED);
    }

    public void setFailure(CallContext context, Throwable exception) {
        this.setProblemState(context, ProblemState.ERROR);
        this.setProblem(context, new ProblemException(context, exception));
        this.setActivityState(context, ActivityState.ABORTED);
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public void setInstruction (CallContext context, Instruction instruction) {
        this.instruction = instruction;
    }

    protected ProblemState problem_state;

    @UIAttribute(Name="ProblemState",Classifier="ProblemState")
    public ProblemState getProblemState (CallContext context) {
        return this.problem_state;
    }

    public void setProblemState (CallContext context, ProblemState problem_state) {
        this.problem_state = problem_state;
    }

    protected Problem problem;

    @UIAttribute(Name="Problem",Value="js:var value = instance.getProblem(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Problem")
    public Problem getProblem (CallContext context) {
        return this.problem;
    }

    public void setProblem (CallContext context, Problem problem) {
        this.problem = problem;
    }

    protected ActivityState activity_state;

    @UIAttribute(Name="ActivityState",Classifier="ActivityState")
    public ActivityState getActivityState (CallContext context) {
        return this.activity_state;
    }

    public void setActivityState (CallContext context, ActivityState activity_state) {
        this.activity_state = activity_state;
    }

    protected Progression progression;

    @UIAttribute(Name="Progression",Classifier="Progression")
    public Progression getProgression (CallContext context) {
        return this.progression;
    }

    public void setProgression (CallContext context, Progression progression) {
        this.progression = progression;
    }

    protected Record record;

    @UIAttribute(Name="Record",Classifier="Record")
    public Record getRecord (CallContext context) {
        return this.record;
    }

    public void setRecord (CallContext context, Record record) {
        this.record = record;
    }

    protected Performance performance;

    @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Performance")
    public Performance getPerformance (CallContext context) {
        return this.performance;
    }

    public Execution wait (CallContext context) {
        return this;
    }

    public String toString() {
        return (this.instruction == null ? "" : this.instruction) + ":" + this.problem_state + "/" + this.activity_state + (this.progression != null ? ("/" + this.progression) : "") + (this.record != null ? ("/" + this.record) : "");
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Execution     ", this.problem_state + "/" + this.activity_state);
        if (this.instruction != null) {
            dump_node.dump(context, "  Instruction ", this.instruction);
        }
        if (this.problem != null) {
            dump_node.dump(context, "  Problem     ", this.problem);
        }
        if (this.progression != null) {
            dump_node.dump(context, "  Progression ", this.progression);
        }
        if (this.record != null) {
            dump_node.dump(context, "  Record      ", this.record);
        }
    }
}
