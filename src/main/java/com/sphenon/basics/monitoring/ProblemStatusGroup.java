package com.sphenon.basics.monitoring;

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

import java.util.Vector;

/**
   One {@link ProblemStatus} is not just enough ;-)
   Reflects a group of problem_statuses, like for a sequence of executions.
*/
public class ProblemStatusGroup extends ProblemStatus {

    protected ProblemStatus[] problem_statuses;

    public ProblemStatusGroup(CallContext context, ProblemStatus... problem_statuses) {
        super(context);
        this.problem_statuses = problem_statuses;
    }

    public ProblemStatusGroup(CallContext context, Vector<ProblemStatus> problem_statuses) {
        super(context);
        this.problem_statuses = new ProblemStatus[0];
        if (problem_statuses != null) {
            this.problem_statuses = problem_statuses.toArray(this.problem_statuses);
        }
    }

    public ProblemState getProblemState (CallContext context) {
        return ProblemState.combineStatuses(context, problem_statuses);
    }

    protected ProblemGroup problems;

    public Problem getProblem (CallContext context) {
        if (this.problems == null) {
            Problem[] pa = new Problem[problem_statuses == null ? 0 : problem_statuses.length];
            if (problem_statuses != null) {
                for (int i=0; i<problem_statuses.length; i++) {
                    pa[i] = problem_statuses[i].getProblem(context);
                }
            }
            this.problems = new ProblemGroup(context, pa);
        }
        return this.problems;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (problem_statuses != null && problem_statuses.length != 0) {
            for (ProblemStatus problem_status : problem_statuses) {
                buffer.append(buffer.length() == 0 ? "[" : ", ");
                buffer.append(problem_status == null ? "--null--" : problem_status.toString());
            }
            buffer.append("]");
        }
        return buffer.toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        DumpNode dn = dump_node.openDump(context, "Problem Statuses");
        int i = 1;
        for (ProblemStatus problem_status : problem_statuses) {
            dn.dump(context, (new Integer(i++)).toString(), problem_status);
        }
        dn.close(context);
    }
}
