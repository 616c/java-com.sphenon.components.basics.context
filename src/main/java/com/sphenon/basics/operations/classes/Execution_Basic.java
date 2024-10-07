package com.sphenon.basics.operations.classes;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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
import com.sphenon.basics.processing.classes.*;

import com.sphenon.basics.function.*;
import com.sphenon.ui.annotations.*;
import com.sphenon.engines.aggregator.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;

public class Execution_Basic implements Execution, Dumpable {

    public Execution_Basic (CallContext context) {
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state) {
        this(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, null, null, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression) {
        this(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, null, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        this(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, record, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record, Getter result_getter) {
        this(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, record, null, result_getter);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record, Object result) {
        this(context, instruction, problem_state, ProblemCategory.UNKNOWN, problem, activity_state, progression, record, result, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state) {
        this(context, instruction, problem_state, problem_category, problem, activity_state, null, null, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression) {
        this(context, instruction, problem_state, problem_category, problem, activity_state, progression, null, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        this(context, instruction, problem_state, problem_category, problem, activity_state, progression, record, null, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record, Object result) {
        this(context, instruction, problem_state, problem_category, problem, activity_state, progression, record, result, null);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record, Getter result_getter) {
        this(context, instruction, problem_state, problem_category, problem, activity_state, progression, record, null, result_getter);
    }

    public Execution_Basic (CallContext context, Instruction instruction, ProblemState problem_state, ProblemCategory problem_category, Problem problem, ActivityState activity_state, Progression progression, Record record, Object result, Getter result_getter) {
        this.instruction      = instruction;
        this.problem_state    = problem_state;
        this.problem_category = problem_category;
        this.problem          = problem;
        this.activity_state   = activity_state;
        this.progression      = progression;
        this.record           = record;
        this.result           = result;
        this.result_getter    = result_getter;
    }

    @OCPIgnore()
    public void setSuccess(CallContext context) {
        this.setProblemState(context, ProblemState.OK);
        this.setProblemCategory(context, ProblemCategory.OK);
        this.setProblem(context, null);
        this.setActivityState(context, ActivityState.COMPLETED);
        this.setProgression(context, Class_Progression.COMPLETED);
    }

    @OCPIgnore()
    public void setIdle(CallContext context) {
        this.setProblemState(context, ProblemState.IDLE);
        this.setProblemCategory(context, ProblemCategory.OK);
        this.setProblem(context, null);
        this.setActivityState(context, ActivityState.COMPLETED);
        this.setProgression(context, Class_Progression.COMPLETED);
    }

    @OCPIgnore()
    public void setSkipped(CallContext context) {
        this.setProblemState(context, ProblemState.IDLE);
        this.setProblemCategory(context, ProblemCategory.OK);
        this.setProblem(context, null);
        this.setActivityState(context, ActivityState.SKIPPED);
        this.setProgression(context, Class_Progression.SKIPPED);
    }

    @OCPIgnore()
    public void setFailure(CallContext context, Throwable exception) {
        this.setProblemState(context, ProblemState.ERROR);
        this.setProblemCategory(context, ProblemCategory.UNKNOWN);
        this.setProblem(context, new ProblemException(context, exception));
        this.setActivityState(context, ActivityState.ABORTED);
    }

    @OCPIgnore()
    public void setFailure(CallContext context, String problem_message) {
        this.setProblemState(context, ProblemState.ERROR);
        this.setProblemCategory(context, ProblemCategory.UNKNOWN);
        this.setProblem(context, new ProblemMessage(context, problem_message));
        this.setActivityState(context, ActivityState.ABORTED);
    }

    @OCPIgnore()
    public void setFailure(CallContext context, ProblemCategory problem_category, Throwable exception) {
        this.setProblemState(context, ProblemState.ERROR);
        this.setProblemCategory(context, problem_category);
        this.setProblem(context, new ProblemException(context, exception));
        this.setActivityState(context, ActivityState.ABORTED);
    }

    @OCPIgnore()
    public void setFailure(CallContext context, ProblemCategory problem_category, String problem_description) {
        this.setProblemState(context, ProblemState.ERROR);
        this.setProblemCategory(context, problem_category);
        this.setProblem(context, new ProblemMessage(context, problem_description));
        this.setActivityState(context, ActivityState.ABORTED);
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public void setInstruction (CallContext context, Instruction instruction) {
        this.instruction = instruction;
    }

    @OCPIgnore()
    public void setInstruction (CallContext context, String instruction_description) {
        this.instruction = new Class_Instruction(context, instruction_description);
    }

    public Instruction defaultInstruction (CallContext context) {
        return null;
    }

    protected ProblemState problem_state;

    @UIAttribute(Name="ProblemState",Classifier="ProblemState")
    public ProblemState getProblemState (CallContext context) {
        return this.problem_state;
    }

    public void setProblemState (CallContext context, ProblemState problem_state) {
        this.problem_state = problem_state;
    }

    protected ProblemCategory problem_category;

    @UIAttribute(Name="ProblemCategory",Classifier="ProblemCategory")
    public ProblemCategory getProblemCategory (CallContext context) {
        return this.problem_category;
    }

    public ProblemCategory defaultProblemCategory (CallContext context) {
        return ProblemCategory.UNKNOWN;
    }

    public void setProblemCategory (CallContext context, ProblemCategory problem_category) {
        this.problem_category = problem_category;
    }

    protected Problem problem;

    @UIAttribute(Name="Problem",Value="js:var value = instance.getProblem(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Problem")
    public Problem getProblem (CallContext context) {
        return this.problem;
    }

    public void setProblem (CallContext context, Problem problem) {
        this.problem = problem;
    }

    public Problem defaultProblem (CallContext context) {
        return null;
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

    public Progression defaultProgression (CallContext context) {
        return null;
    }

    protected Record record;

    @UIAttribute(Name="Record",Value="js:var value = instance.getRecord(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Record")
    public Record getRecord (CallContext context) {
        return this.record;
    }

    public void setRecord (CallContext context, Record record) {
        this.record = record;
    }

    public Record defaultRecord (CallContext context) {
        return null;
    }

    protected Performance performance;

    @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Performance")
    public Performance getPerformance (CallContext context) {
        return this.performance;
    }

    public void setPerformance (CallContext context, Performance performance) {
        this.performance = performance;
    }

    public Performance defaultPerformance (CallContext context) {
        return null;
    }

    protected Object result;
    protected Getter result_getter;

    public Object getResult (CallContext context) {
        return   this.result != null ? this.result
               : this.result_getter != null ? this.result_getter.get(context)
               : null;
    }

    public void setResult (CallContext context, Object result) {
        this.result = result;
    }

    public Object defaultResult (CallContext context) {
        return null;
    }

    public void setResultGetter (CallContext context, Getter result_getter) {
        this.result_getter = result_getter;
    }

    public Getter defaultResultGetter (CallContext context) {
        return null;
    }

    public Execution wait (CallContext context) {
        return this;
    }

    public String toString() {
        return (this.instruction == null ? "" : this.instruction) + ":" + this.problem_state + "/" + this.activity_state + (this.progression != null ? ("/" + this.progression) : "") + (this.record != null ? ("/" + this.record) : "");
    }

    public String toMessage() {
        return toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Execution     ", this.problem_state + (this.problem_category != null ? ("(" + this.problem_category + ")") : "") + "/" + this.activity_state);
        if (this.instruction != null) {
            dump_node.dump(context, "  Instruction ", this.instruction);
        }
        if (this.progression != null) {
            dump_node.dump(context, "  Progression ", this.progression);
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (this.problem != null) {
                dump_node.dump(context, "  Problem     ", this.problem);
            }
            if (this.record != null) {
                dump_node.dump(context, "  Record      ", this.record);
            }
        }
    }
}
