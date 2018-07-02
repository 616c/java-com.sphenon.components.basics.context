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

@UIId         ("activity_state")
@UIName       ("ActivityState")
@UIClassifier ("ActivityState")
public class ActivityState implements UIEquipped {

    static public ActivityState ATREST      ; // a process activity which is momentarily inactive,
                                              // since it is not needed; and it is unknown whether
                                              // it's ready or unready

    static public ActivityState UNREADY     ; // cannot start for internal or external reasons
                                              // e.g. resource occupied otherwise
                                              // e.g. prerequisites (material and tools) not available
    static public ActivityState READY       ; // startable
                                              // i.e. everything available, but not requested
    static public ActivityState INPROGRESS  ; // working
    static public ActivityState INTERRUPTED ; // some internal or external condition has to
                                              // be fulfilled before proceeding; dependend
                                              // activities are expected to wait; comprises 'suspended',
                                              // i.e. interruption by explicit request
                                              // or e.g. prerequisites are not available yet
                                              // another example is: a task failed but is
                                              // rescheduled for another trial
    static public ActivityState ABORTED     ; // not completed, either because of failure
                                              // or for internal/scheduling/control reasons
    static public ActivityState COMPLETED   ; // completed successfully

    // move to something like "Übergabe"
    // (related terms: delivered, verified (where, when, by whom?), accepted, rejected, notified?)
    static public ActivityState VERIFIED    ; // postconditions are fulfilled/target states achieved

    // ...maybe... this belongs to CoordinationState
    static public ActivityState SKIPPED     ; // processing was intentionally not performed,
                                              // it was not necessary to execute the activity
                                              // and it will not be in the future

    static protected final int ATREST_INDEX      = 0;
    static protected final int UNREADY_INDEX     = 1;
    static protected final int READY_INDEX       = 2;
    static protected final int INPROGRESS_INDEX  = 3;
    static protected final int INTERRUPTED_INDEX = 4;
    static protected final int ABORTED_INDEX     = 5;
    static protected final int COMPLETED_INDEX   = 6;
    static protected final int VERIFIED_INDEX    = 7;
    static protected final int SKIPPED_INDEX     = 8;

    static public final String list_of_states = "atrest,unready,ready,inprogress,interrupted,aborted,completed,verified,skipped,unknown";

    public ActivityState combineWith(CallContext context, ActivityState other) {
        return combineWith(context, other, false);
    }

    public ActivityState combineWith(CallContext context, ActivityState other, boolean as_alternatives) {
        if (as_alternatives) {
            if (this == VERIFIED    || other == VERIFIED)    { return VERIFIED;    }
            if (this == COMPLETED   || other == COMPLETED)   { return COMPLETED;   }
            if (this == INPROGRESS  || other == INPROGRESS)  { return INPROGRESS;  }
            if (this == READY       || other == READY)       { return READY;       }
            if (this == UNREADY     || other == UNREADY)     { return UNREADY;     }
            if (this == INTERRUPTED || other == INTERRUPTED) { return INTERRUPTED; }
            if (this == ABORTED     || other == ABORTED)     { return ABORTED;     }
            if (this == SKIPPED     || other == SKIPPED)     { return SKIPPED;     }
            if (this == ATREST      || other == ATREST)      { return ATREST;      }
            System.err.println("ActivityState.java (1): invalid states " + this.index + " - " + other.index);
            return COMPLETED;
        } else {
            if (this == ABORTED     || other == ABORTED)     { return ABORTED;     }
            if (this == INPROGRESS  || other == INPROGRESS)  { return INPROGRESS;  }
            if (this == INTERRUPTED || other == INTERRUPTED) { return INTERRUPTED; }
            if (this == UNREADY     || other == UNREADY)     { return UNREADY;     }
            if (this == READY       || other == READY)       { return READY;       }
            if (this == COMPLETED   || other == COMPLETED)   { return COMPLETED;   }
            if (this == VERIFIED    || other == VERIFIED)    { return VERIFIED;    }
            if (this == SKIPPED     || other == SKIPPED)     { return SKIPPED;     }
            if (this == ATREST      || other == ATREST)      { return ATREST;      }
            System.err.println("ActivityState.java (2): invalid states " + this.index + " - " + other.index);
            return COMPLETED;
        }
    }

    int index;

    protected ActivityState(int index) {
        this.index = index;
    }

    public int getIndex (CallContext cc) {
        return this.index;
    }

    static protected Map<Integer, ActivityState> map;

    static public ActivityState getByIndex(CallContext context, int index) {
        if (map == null) {
            map = new HashMap<Integer, ActivityState>();
            map.put(ATREST.index      , ATREST);
            map.put(UNREADY.index     , UNREADY);
            map.put(READY.index       , READY);
            map.put(INPROGRESS.index  , INPROGRESS);
            map.put(INTERRUPTED.index , INTERRUPTED);
            map.put(ABORTED.index     , ABORTED);
            map.put(COMPLETED.index   , COMPLETED);
            map.put(VERIFIED.index    , VERIFIED);
            map.put(SKIPPED.index     , SKIPPED);
        }
        return map.get(index);
    }

    static {
        ATREST           = new ActivityState(ATREST_INDEX);
        UNREADY          = new ActivityState(UNREADY_INDEX);
        READY            = new ActivityState(READY_INDEX);
        INPROGRESS       = new ActivityState(INPROGRESS_INDEX);
        INTERRUPTED      = new ActivityState(INTERRUPTED_INDEX);
        ABORTED          = new ActivityState(ABORTED_INDEX);
        COMPLETED        = new ActivityState(COMPLETED_INDEX);
        VERIFIED         = new ActivityState(VERIFIED_INDEX);
        SKIPPED          = new ActivityState(SKIPPED_INDEX);
    }

    public String toString() {
        switch (index) {
            case ATREST_INDEX          : return "atrest";
            case UNREADY_INDEX         : return "unready";
            case READY_INDEX           : return "ready";
            case INPROGRESS_INDEX      : return "inprogress";
            case INTERRUPTED_INDEX     : return "interrupted";
            case ABORTED_INDEX         : return "aborted";
            case COMPLETED_INDEX       : return "completed";
            case VERIFIED_INDEX        : return "verified";
            case SKIPPED_INDEX         : return "skipped";
            default                    : return "[?]";
        }
    }

    public String toStringMC() {
        switch (index) {
            case ATREST_INDEX          : return "atrest";
            case UNREADY_INDEX         : return "unready";
            case READY_INDEX           : return "ready";
            case INPROGRESS_INDEX      : return "inprogress";
            case INTERRUPTED_INDEX     : return "interrupted";
            case ABORTED_INDEX         : return "aborted";
            case COMPLETED_INDEX       : return "completed";
            case VERIFIED_INDEX        : return "verified";
            case SKIPPED_INDEX         : return "skipped";
            default                    : return "[?]";
        }
    }

    static protected Vector<Object> choice_set;

    static public Vector<Object> getChoiceSet(CallContext context) {

        if (choice_set == null) {
            choice_set = new Vector<Object>();

            choice_set.add(ATREST);
            choice_set.add(UNREADY);
            choice_set.add(READY);
            choice_set.add(INPROGRESS);
            choice_set.add(INTERRUPTED);
            choice_set.add(ABORTED);
            choice_set.add(COMPLETED);
            choice_set.add(VERIFIED);
            choice_set.add(SKIPPED);
        }

        return choice_set;
    }

    static protected Map<String,Vector<UIEquipment>> ui_equipment_map;

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        if (ui_equipment_map == null) {
            ui_equipment_map = (Map<String,Vector<UIEquipment>>) com.sphenon.engines.aggregator.Aggregator.create(context, "com/sphenon/basics/processing/ActivityState_UIEquipments");
        }
        return ui_equipment_map == null ? null : ui_equipment_map.get(toString());
    }

    public Vector<UIEquipment> getUIEquipments(CallContext context, String feature) {
        return null;
    }
}
