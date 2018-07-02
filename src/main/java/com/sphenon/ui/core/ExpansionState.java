package com.sphenon.ui.core;

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
import com.sphenon.ui.core.classes.*;

import java.util.Vector;

public enum ExpansionState implements UIEquipped {
    Hidden   (1), // completely invisible
    Closed   (2), // a hint that it exists (e.g. open button)
    Hint     (3), // some, typically incomplete, information is shown ("get the idea")
    Identity (4), // the minimal amount of uniquely identifying information is shown
    Preview  (5), // a more or less reduced version (thumbnail, essential attributes etc.)
    Open     (6), // "normal" information
    Detailed (7), // more information than usual
    Complete (8), // every information available
    Spreaded (9); // the boundary is dissolved, items are spreaded into the container, "unboxed"

    private int index;
    private ExpansionState(int index) { this.index = index; }

    public ExpansionState reduce(CallContext context) {
        // the order here is intended and used this way in SVG diagram renderer
        switch (this) {
            case Hidden  : return Hidden;
            case Closed  : return Closed;
            case Hint    : return Closed;
            case Identity: return Hint;
            case Preview : return Identity;
            case Open    : return Preview;
            case Detailed: return Open;
            case Complete: return Open;
            case Spreaded: return Open;
            default      : return null;
        }
    }

    public ExpansionState expand(CallContext context) {
        // the order here is intended and used this way in SVG diagram renderer
        switch (this) {
            case Hidden  : return Closed;
            case Closed  : return Hint;
            case Hint    : return Identity;
            case Identity: return Preview;
            case Preview : return Open;
            case Open    : return Spreaded;
            case Detailed: return Spreaded;
            case Complete: return Spreaded;
            case Spreaded: return Spreaded;
            default      : return null;
        }
    }

    public boolean is (ExpansionState... ess)   {   for (ExpansionState es : ess) {
                                                        if (es == this) { return true; }
                                                    }
                                                    return false;
                                                }
    public boolean less(ExpansionState es)      {  return this.index < es.index;  }
    public boolean more(ExpansionState es)      {  return this.index > es.index;  }
    public boolean leeq(ExpansionState es)      {  return this.index <= es.index;  }
    public boolean moeq(ExpansionState es)      {  return this.index >= es.index;  }

    // static protected Map<String,Vector<UIEquipment>> ui_equipment_map;

    protected Vector<UIEquipment> ui_equipments;

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        if (this.ui_equipments == null) {
            // if (ui_equipment_map == null) {
            //     ui_equipment_map = (Map<String,Vector<UIEquipment>>) com.sphenon.engines.aggregator.Aggregator.create(context, "com/sphenon/ui/core/ExpansionState_UIEquipments");
            // }
            // this.ui_equipments = (ui_equipment_map == null ? null : ui_equipment_map.get(toString()));
            if (this.ui_equipments == null) {
                this.ui_equipments = new Vector<UIEquipment>();
                this.ui_equipments.add(new Class_UIEquipment(context, UIEquipmentType.Name, toString()));
            }
        }
        return this.ui_equipments;
    }
}
