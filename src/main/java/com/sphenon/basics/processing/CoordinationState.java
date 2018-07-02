package com.sphenon.basics.processing;

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
import com.sphenon.ui.core.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import com.sphenon.ui.annotations.*;

@UIId         ("coordination_state")
@UIName       ("CoordinationState")
@UIClassifier ("CoordinationState")
public enum CoordinationState implements UIEquipped {

    MustNotExecute   (0, "must_not_execute"),
    ShouldNotExecute (1, "should_not_execute"),
    MayExecute       (2, "may_execute"),
    ShouldExecute    (3, "should_execute"),
    MustExecute      (4, "must_execute"),
    Executed         (5, "executed");

    static public final String list_of_states = "must_not_execute,should_not_execute,may_execute,should_execute,must_execute,executed";

    public CoordinationState combineWith(CallContext context, CoordinationState other) {
        if (this == MustExecute      || other == MustExecute     ) { return MustExecute;      }
        if (this == ShouldExecute    || other == ShouldExecute   ) { return ShouldExecute;    }
        if (this == MayExecute       || other == MayExecute      ) { return MayExecute;       }
        if (this == ShouldNotExecute || other == ShouldNotExecute) { return ShouldNotExecute; }
        if (this == MustNotExecute   || other == MustNotExecute  ) { return MustNotExecute;   }
        if (this == Executed         || other == Executed        ) { return Executed;   }
        return Executed; // shoud be impossible
    }

    CoordinationState(int index, String id) {
        this.index = index;
        this.id = id;
    }

    int index;

    public int getIndex (CallContext context) {
        return this.index;
    }

    String id;

    public String getId (CallContext context) {
        return this.id;
    }

    public boolean canExecute(CallContext context) {
        switch(this) {
            case MustNotExecute  : return false;
            case ShouldNotExecute: return true;
            case MayExecute      : return true;
            case ShouldExecute   : return true;
            case MustExecute     : return true;
            case Executed        : return false;
        }
        return false;
    }

    public boolean mustExecute(CallContext context) {
        switch(this) {
            case MustNotExecute  : return false;
            case ShouldNotExecute: return false;
            case MayExecute      : return false;
            case ShouldExecute   : return false;
            case MustExecute     : return true;
            case Executed        : return false;
        }
        return false;
    }

    static protected Map<Integer, CoordinationState> map;

    static public CoordinationState getByIndex (CallContext context, int index) {
        if (map == null) {
            map = new HashMap<Integer, CoordinationState>();
            map.put(MustNotExecute.index  , MustNotExecute  );
            map.put(ShouldNotExecute.index, ShouldNotExecute);
            map.put(MayExecute.index      , MayExecute      );
            map.put(ShouldExecute.index   , ShouldExecute   );
            map.put(MustExecute.index     , MustExecute     );
            map.put(Executed.index        , Executed        );
        }
        return map.get(index);
    }

    public String toString() {
        return this.id;
    }

    static protected Vector<Object> choice_set;

    static public Vector<Object> getChoiceSet(CallContext context) {
        if (choice_set == null) {
            choice_set = new Vector<Object>();
            choice_set.add(MustNotExecute);
            choice_set.add(ShouldNotExecute);
            choice_set.add(MayExecute);
            choice_set.add(ShouldExecute);
            choice_set.add(MustExecute);
            choice_set.add(Executed);
        }

        return choice_set;
    }

    static protected Map<Integer,Vector<UIEquipment>> ui_equipment_map;

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        if (ui_equipment_map == null) {
            ui_equipment_map = (Map<Integer,Vector<UIEquipment>>) com.sphenon.engines.aggregator.Aggregator.create(context, "com/sphenon/basics/processing/CoordinationState_UIEquipments");
        }
        return ui_equipment_map == null ? null : ui_equipment_map.get(toString());
    }

    public Vector<UIEquipment> getUIEquipments(CallContext context, String feature) {
        return null;
    }
}
