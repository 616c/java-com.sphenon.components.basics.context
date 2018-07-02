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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.debug.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;
import java.util.Vector;

@UIParts("js:instance.getExecutions(context)")
public class Execution_BasicSequence implements Execution, Dumpable, ContextAware {

    protected Vector<Execution> executions;

    // static protected long all         = 0;
    // static protected long closed_ones = 0;

    public Execution_BasicSequence (CallContext context, Instruction instruction, Execution... executions) {
        this.instruction = instruction;
        this.executions = new Vector<Execution>();
        for (Execution execution : executions) {
            assert(execution != null);
            this.addExecution(context, execution, false);
        }
        // all++;
        // System.err.println("BasicSequence: " + all + "/" + closed_ones);
    }

    static public Execution_BasicSequence createExecutionSequence(CallContext context, Instruction instruction, Execution... executions) {
        return new Execution_BasicSequence (context, instruction, executions);
    }

    static public Execution_BasicSequence createExecutionSequence(CallContext context, String instruction_description, Execution... executions) {
        return new Execution_BasicSequence (context, new Class_Instruction(context, instruction_description), executions);
    }

    public Vector<Execution> getExecutions(CallContext context) {
        return this.executions;
    }

    public void addExecution(CallContext context, Execution execution) {
        this.addExecution(context, execution, true);
    }

    public void addExecution(CallContext context, Execution execution, boolean notify) {
        this.executions.add(execution);
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public ProblemState getProblemState (CallContext context) {
        ProblemState problem_state = ProblemState.IDLE;
        for (Execution execution : this.executions) {
            problem_state = problem_state.combineWith(context, execution.getProblemState(context));
        }
        return problem_state;
    }

    public Problem getProblem (CallContext context) {
        Problem[] problems = new Problem[executions.size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.executions) {
            if ((problems[i++] = execution.getProblem(context)) != null) {
                got_one = true;
            }
        }
        return got_one ? new ProblemGroup(context, problems) : null;
    }

    protected boolean closed;

    public void close(CallContext context) {
        this.closed = true;
        // closed_ones++;
        // System.err.println("BasicSequence: " + all + "/" + closed_ones);
    }

    public ActivityState getActivityState (CallContext context) {
        if (this.executions == null || this.executions.size() == 0) {
            return this.closed ? ActivityState.COMPLETED : ActivityState.UNREADY;
        }
        ActivityState activity_state = ActivityState.COMPLETED;
        for (Execution execution : this.executions) {
            activity_state = activity_state.combineWith(context, execution.getActivityState(context));
        }
        return activity_state;
    }

    public Progression getProgression (CallContext context) {
        Progression[] progressions = new Progression[executions.size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.executions) {
            if ((progressions[i++] = execution.getProgression(context)) != null) {
                got_one = true;
            }
        }
        return got_one ? new ProgressionGroup(context, progressions) : null;
    }

    public Record getRecord (CallContext context) {
        Record[] records = new Record[executions.size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.executions) {
            if ((records[i++] = execution.getRecord(context)) != null) {
                got_one = true;
            }
        }
        return got_one ? new RecordGroup(context, records) : null;
    }

    @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)")
    public Performance getPerformance (CallContext context) {
        if (this.executions == null || this.executions.size() == 0) {
            return null;
        }
        Performance performance = null; // ...
        for (Execution execution : this.executions) {
            // performance = performance.add(context, execution.getPerformance(context));
        }
        return performance;
    }

    public Execution wait (CallContext context) {
        for (Execution execution : this.executions) {
            execution.wait(context);
        }
        return this;
    }

    public String toString(CallContext context) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);
        Instruction   instruction    = getInstruction(context);

        return problem_state + "/" + activity_state + (progression != null ? ("/" + ContextAware.ToString.convert(context, progression)) : "") + (instruction != null ? ("/" + ContextAware.ToString.convert(context, instruction)) : "") + (record != null ? ("/" + ContextAware.ToString.convert(context, record)) : "");
    }

    public String toString() {
        return toString(RootContext.getFallbackCallContext());
    }

    public void dump(CallContext context, DumpNode dump_node) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);

        dump_node.dump(context, "Execution    ", problem_state + "/" + activity_state);
        if (problem != null) {
            dump_node.dump(context, "- Problem    ", problem);
        }
        if (progression != null) {
            dump_node.dump(context, "- Progression", progression);
        }
        if (record != null) {
            dump_node.dump(context, "- Record     ", record);
        }
        DumpNode dn = dump_node.openDump(context, "- Executions ");
        int i=1;
        for (Execution execution : this.executions) {
            dn.dump(context, (new Integer(i++)).toString(), execution);
        }
        dn.close(context);
    }
}
