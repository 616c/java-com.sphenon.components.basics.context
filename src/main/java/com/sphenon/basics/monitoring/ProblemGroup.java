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
   One {@link Problem} is not just enough ;-)
   Reflects a group of problems, like for a sequence of executions.
*/
public class ProblemGroup implements Problem, Dumpable {

    protected Problem[] problems;

    public ProblemGroup(CallContext context, Problem... problems) {
        this.problems = problems;
    }

    public ProblemGroup(CallContext context, Vector<Problem> problems) {
        this.problems = new Problem[0];
        if (problems != null) {
            this.problems = problems.toArray(this.problems);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (problems != null && problems.length != 0) {
            for (Problem problem : problems) {
                buffer.append(buffer.length() == 0 ? "[" : ", ");
                buffer.append(problem == null ? "--null--" : problem.toString());
            }
            buffer.append("]");
        }
        return buffer.toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        DumpNode dn = dump_node.openDump(context, "Problems");
        int i = 1;
        for (Problem problem : problems) {
            dn.dump(context, (new Integer(i++)).toString(), problem);
        }
        dn.close(context);
    }
}
