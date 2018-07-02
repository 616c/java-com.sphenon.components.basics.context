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
import com.sphenon.ui.core.*;
import com.sphenon.ui.core.classes.*;

import java.util.Vector;

public class ProblemStatus implements Dumpable, Problem, UIEquipped {

    public ProblemStatus (CallContext context, ProblemState problem_state, Problem problem) {
        this.problem_state = problem_state;
        this.problem = problem;
    }

    public ProblemStatus (CallContext context, ProblemState problem_state, String message) {
        this(context, problem_state, new ProblemMessage(context, message));
    }

    public ProblemStatus (CallContext context, ProblemState problem_state, Throwable exception) {
        this(context, problem_state, new ProblemException(context, exception));
    }

    public ProblemStatus (CallContext context, ProblemState problem_state, int return_code) {
        this(context, problem_state, new ProblemReturnCode(context, return_code));
    }

    protected ProblemStatus (CallContext context) {
    }

    static protected Problem getProblem(CallContext context, MonitorableObject monitorable_object) {
        Vector<ProblemStatus> pss = monitorable_object.getProblemStatusDetails(context);
        Problem p = (pss != null && pss.size() == 1 ? pss.get(0) : new ProblemGroup(context, (Vector) pss /* somewhat böser cast */));
        return p;
    }

    public ProblemStatus (CallContext context, MonitorableObject monitorable_object) {
        this(context, monitorable_object.getProblemState(context), getProblem(context, monitorable_object));
    }

    protected ProblemState problem_state;

    public ProblemState getProblemState (CallContext context) {
        return this.problem_state;
    }

    public void setProblemState (CallContext context, ProblemState problem_state) {
        this.problem_state = problem_state;
    }

    protected Problem problem;

    public Problem getProblem (CallContext context) {
        return this.problem;
    }

    public void setProblem (CallContext context, Problem problem) {
        this.problem = problem;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(this.problem_state.toString());
        if (this.problem != null) {
            buffer.append(",");
            buffer.append(this.problem.toString());
        }
        buffer.append("]");
        return buffer.toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        DumpNode dn = dump_node.openDump(context, "Problem Status");
        dn.dump(context, "Problem State", this.problem_state);
        dn.dump(context, "Problem", this.problem);
    }

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        Vector<UIEquipment> ui_equipments = new Vector<UIEquipment>(4);
        if (this.problem_state != null) {
            for (UIEquipment uie : this.problem_state.getUIEquipments(context)) {
                ui_equipments.add(uie);
            }
        }
        if (this.problem != null) {
            ui_equipments.add(new Class_UIEquipment(context, UIEquipmentType.ShortDescription, ContextAware.ToString.convert(context, this.problem)));
        }
        return ui_equipments;
    }

    public Vector<UIEquipment> getUIEquipments(CallContext context, String feature) {
        return null;
    }
}
