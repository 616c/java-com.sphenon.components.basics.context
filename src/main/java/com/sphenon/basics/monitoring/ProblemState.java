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
import com.sphenon.ui.core.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import com.sphenon.ui.annotations.*;

@UIId         ("problemstate")
@UIName       ("ProblemState")
@UIClassifier ("ProblemState")
public class ProblemState implements UIEquipped {


    static public ProblemState IDLE                      ;
    static public ProblemState IDLE_INCOMPLETE           ;
                                                // (blue)
                                                // basically same as "ok", but
                                                // usable in cases where there's
                                                // actually nothing happening at all
                                                // which could be not ok (e.g. out
                                                // of operation)
                                                // (or, in conjunction with "information_complete" == false
                                                // means "nothing happened so far which could
                                                // be not ok", if, e.g., in a sequence of
                                                // steps no step was executed at all)
                                                // (another example is "execution skipped")

    static public ProblemState OK                        ;
    static public ProblemState OK_INCOMPLETE             ;
                                                // (gruen)
                                                // everything's fine

    static public ProblemState INFO                      ;
    static public ProblemState INFO_INCOMPLETE           ;
                                                // (gruen)
                                                // in case someone is interested

    static public ProblemState NOTICE                    ;
    static public ProblemState NOTICE_INCOMPLETE         ;
                                                // ((gelbgruen))
                                                // please take into account
                                                // (it is imaginable this is of interest to someone,
                                                // but cannot be judged from the perspective of the
                                                // sender; actually no definitive risk is identified,
                                                // it may be ok but it may also be not)

    static public ProblemState UNKNOWN                   ;
    static public ProblemState UNKNOWN_INCOMPLETE        ;
                                                // (black)
                                                // no information available at present
                                                // (e.g. no connection)
                                                // in conjunction with incomplete: still something is ongoing
                                                // which might retrieve the information, e.g. no timeout yet

    static public ProblemState CAUTION                   ;
    static public ProblemState CAUTION_INCOMPLETE        ;
                                                // (gelb)
                                                // something is not proper, special
                                                // attention may be required, but no
                                                // concrete risk identified

    static public ProblemState WARNING                   ;
    static public ProblemState WARNING_INCOMPLETE        ;
                                                // (gelb)
                                                // there is a conrete risk if no attention is paid,
                                                // you better take into account ("you've been warned")

    static public ProblemState SEVERE_WARNING            ;
    static public ProblemState SEVERE_WARNING_INCOMPLETE ;
                                                // ((orange))
                                                // it's critical to take into account;
                                                // maybe already some dysfunctioning occured,
                                                // with no "real" damage yet, but important
                                                // (e.g. performance decrease)
                                                // processing can still continue, but
                                                // probably "handicapped"

    static public ProblemState ERROR                     ;
    static public ProblemState ERROR_INCOMPLETE          ;
                                                // (rot)
                                                // operation/system is malfunctioning,
                                                // normal processing cannot continue, either
                                                // temporarily or finally; external
                                                // intervention may be required

    static public ProblemState CRITICAL_ERROR            ;
    static public ProblemState CRITICAL_ERROR_INCOMPLETE ;
                                                // ((rot blinkend))
                                                // there is a risk of damage

    static public ProblemState FATAL_ERROR               ;
    static public ProblemState FATAL_ERROR_INCOMPLETE    ;
                                                // ((rot blinkend))
                                                // damage has already been detected

    static public ProblemState EMERGENCY                 ;
    static public ProblemState EMERGENCY_INCOMPLETE      ;
                                                // ((rot blinkend, sirene))
                                                // damage is in progress, immediate action
                                                // is indispensable

    static public ProblemState PANIC                     ;
    static public ProblemState PANIC_INCOMPLETE          ;
                                                // system completely out of control;
                                                // no reliable statements possible

    static public final int IDLE_INDEX            =  0;
    static public final int OK_INDEX              =  1;
    static public final int INFO_INDEX            =  2;
    static public final int NOTICE_INDEX          =  3;
    static public final int UNKNOWN_INDEX         =  4;
    static public final int CAUTION_INDEX         =  5;
    static public final int WARNING_INDEX         =  6;
    static public final int SEVERE_WARNING_INDEX  =  7;
    static public final int ERROR_INDEX           =  8;
    static public final int CRITICAL_ERROR_INDEX  =  9;
    static public final int FATAL_ERROR_INDEX     = 10;
    static public final int EMERGENCY_INDEX       = 11;
    static public final int PANIC_INDEX           = 12;

    static public final String list_of_states_1 = "idle,ok,info,notice,unknown,caution,warning,severe_warning,error,critical_error,fatal_error,emergency,panic";
    static public final String list_of_states_2 = "complete,incomplete";

    static protected int[][][] RGB = {
        { {   0,   0, 255 }, {   0,   0, 128 } }, // IDLE
        { {   0, 192,   0 }, {   0,  96,   0 } }, // OK
        { {   0, 255,   0 }, {   0, 128,   0 } }, // INFO
        { { 192, 255,   0 }, {  96, 128,   0 } }, // NOTICE
        { { 192, 192, 192 }, { 128, 128, 128 } }, // UNKNOWN
        { { 255, 255,   0 }, { 128, 128,   0 } }, // CAUTION
        { { 255, 224,   0 }, { 128, 112,   0 } }, // WARNING
        { { 255, 160,   0 }, { 128,  80,   0 } }, // SEVERE_WARNING
        { { 255,   0,   0 }, { 128,   0,   0 } }, // ERROR
        { { 255,   0,   0 }, { 128,   0,   0 } }, // CRITICAL_ERROR
        { { 255,   0,   0 }, { 128,   0,   0 } }, // FATAL_ERROR
        { { 255,   0,   0 }, { 128,   0,   0 } }, // EMERGENCY
        { { 255,   0,   0 }, { 128,   0,   0 } }, // PANIC
    };

    static protected String[][] RGBHex = {
        { "0000FF", "000080" }, // IDLE
        { "00C000", "006000" }, // OK
        { "00FF00", "008000" }, // INFO
        { "C0FF00", "608000" }, // NOTICE
        { "C0C0C0", "808080" }, // UNKNOWN
        { "FFFF00", "808000" }, // CAUTION
        { "FFE000", "807000" }, // WARNING
        { "FFA000", "805000" }, // SEVERE_WARNING
        { "FF0000", "800000" }, // ERROR
        { "FF0000", "800000" }, // CRITICAL_ERROR
        { "FF0000", "800000" }, // FATAL_ERROR
        { "FF0000", "800000" }, // EMERGENCY
        { "FF0000", "800000" }, // PANIC
    };

    static public ProblemState[] ALL_STATES;
    static public ProblemState[] RED_STATES;
    static public ProblemState[] YELLOW_RED_STATES;
    static public ProblemState[] YELLOW_STATES;
    static public ProblemState[] GREEN_YELLOW_STATES;
    static public ProblemState[] GREEN_STATES;

    public int[] getRGB(CallContext cc) {
        return RGB[this.index][this.information_complete ? 0 : 1];
    }

    protected int     index;
    protected boolean information_complete;
    protected String  id;

    protected ProblemState(int index, boolean information_complete, String id) {
        this.index = index;
        this.information_complete = information_complete;
        this.id = id;
        state_map.put(id, this);
    }

    public int getIndex (CallContext cc) {
        return this.index;
    }

    public int getDBIndex (CallContext context) {
        return this.index * 2 + (information_complete ? 0 : 1);
    }

    static public ProblemState getByDBIndex (CallContext context, int db_index) {
        int index = db_index / 2;
        boolean information_complete = ((db_index - (index * 2)) == 0 ? true : false);
        return getProblemState(context, index, information_complete);
    }

    public String getId (CallContext cc) {
        return this.id;
    }

    public boolean getInformationComplete (CallContext cc) {
        return this.information_complete;
    }

    static Map<String,ProblemState> state_map;

    static {
        state_map = new HashMap<String,ProblemState>();

        IDLE                         = new ProblemState(IDLE_INDEX, true, "IDLE");
        IDLE_INCOMPLETE              = new ProblemState(IDLE_INDEX, false, "IDLE_INCOMPLETE");
        OK                           = new ProblemState(OK_INDEX, true, "OK");
        OK_INCOMPLETE                = new ProblemState(OK_INDEX, false, "OK_INCOMPLETE");
        INFO                         = new ProblemState(INFO_INDEX, true, "INFO");
        INFO_INCOMPLETE              = new ProblemState(INFO_INDEX, false, "INFO_INCOMPLETE");
        NOTICE                       = new ProblemState(NOTICE_INDEX, true, "NOTICE");
        NOTICE_INCOMPLETE            = new ProblemState(NOTICE_INDEX, false, "NOTICE_INCOMPLETE");
        UNKNOWN                      = new ProblemState(UNKNOWN_INDEX, true, "UNKNOWN");
        UNKNOWN_INCOMPLETE           = new ProblemState(UNKNOWN_INDEX, false, "UNKNOWN_INCOMPLETE");
        CAUTION                      = new ProblemState(CAUTION_INDEX, true, "CAUTION");
        CAUTION_INCOMPLETE           = new ProblemState(CAUTION_INDEX, false, "CAUTION_INCOMPLETE");
        WARNING                      = new ProblemState(WARNING_INDEX, true, "WARNING");
        WARNING_INCOMPLETE           = new ProblemState(WARNING_INDEX, false, "WARNING_INCOMPLETE");
        SEVERE_WARNING               = new ProblemState(SEVERE_WARNING_INDEX, true, "SEVERE_WARNING");
        SEVERE_WARNING_INCOMPLETE    = new ProblemState(SEVERE_WARNING_INDEX, false, "SEVERE_WARNING_INCOMPLETE");
        ERROR                        = new ProblemState(ERROR_INDEX, true, "ERROR");
        ERROR_INCOMPLETE             = new ProblemState(ERROR_INDEX, false, "ERROR_INCOMPLETE");
        CRITICAL_ERROR               = new ProblemState(CRITICAL_ERROR_INDEX, true, "CRITICAL_ERROR");
        CRITICAL_ERROR_INCOMPLETE    = new ProblemState(CRITICAL_ERROR_INDEX, false, "CRITICAL_ERROR_INCOMPLETE");
        FATAL_ERROR                  = new ProblemState(FATAL_ERROR_INDEX, true, "FATAL_ERROR");
        FATAL_ERROR_INCOMPLETE       = new ProblemState(FATAL_ERROR_INDEX, false, "FATAL_ERROR_INCOMPLETE");
        EMERGENCY                    = new ProblemState(EMERGENCY_INDEX, true, "EMERGENCY");
        EMERGENCY_INCOMPLETE         = new ProblemState(EMERGENCY_INDEX, false, "EMERGENCY_INCOMPLETE");
        PANIC                        = new ProblemState(PANIC_INDEX, true, "PANIC");
        PANIC_INCOMPLETE             = new ProblemState(PANIC_INDEX, false, "PANIC_INCOMPLETE");

        ALL_STATES                   = makeStates(IDLE, IDLE_INCOMPLETE, OK, OK_INCOMPLETE, INFO, INFO_INCOMPLETE, NOTICE, NOTICE_INCOMPLETE, UNKNOWN, UNKNOWN_INCOMPLETE, CAUTION, CAUTION_INCOMPLETE, WARNING, WARNING_INCOMPLETE, SEVERE_WARNING, SEVERE_WARNING_INCOMPLETE, ERROR, ERROR_INCOMPLETE, CRITICAL_ERROR, CRITICAL_ERROR_INCOMPLETE, FATAL_ERROR, FATAL_ERROR_INCOMPLETE, EMERGENCY, EMERGENCY_INCOMPLETE, PANIC, PANIC_INCOMPLETE);
        RED_STATES                   = makeStates(ERROR, ERROR_INCOMPLETE, CRITICAL_ERROR, CRITICAL_ERROR_INCOMPLETE, FATAL_ERROR, FATAL_ERROR_INCOMPLETE, EMERGENCY, EMERGENCY_INCOMPLETE, PANIC, PANIC_INCOMPLETE);
        YELLOW_RED_STATES            = makeStates(UNKNOWN, UNKNOWN_INCOMPLETE, CAUTION, CAUTION_INCOMPLETE, WARNING, WARNING_INCOMPLETE, SEVERE_WARNING, SEVERE_WARNING_INCOMPLETE, ERROR, ERROR_INCOMPLETE, CRITICAL_ERROR, CRITICAL_ERROR_INCOMPLETE, FATAL_ERROR, FATAL_ERROR_INCOMPLETE, EMERGENCY, EMERGENCY_INCOMPLETE, PANIC, PANIC_INCOMPLETE);
        YELLOW_STATES                = makeStates(UNKNOWN, UNKNOWN_INCOMPLETE, CAUTION, CAUTION_INCOMPLETE, WARNING, WARNING_INCOMPLETE, SEVERE_WARNING, SEVERE_WARNING_INCOMPLETE);
        GREEN_YELLOW_STATES          = makeStates(IDLE, IDLE_INCOMPLETE, OK, OK_INCOMPLETE, INFO, INFO_INCOMPLETE, NOTICE, NOTICE_INCOMPLETE, UNKNOWN, UNKNOWN_INCOMPLETE, CAUTION, CAUTION_INCOMPLETE, WARNING, WARNING_INCOMPLETE, SEVERE_WARNING, SEVERE_WARNING_INCOMPLETE);
        GREEN_STATES                 = makeStates(IDLE, IDLE_INCOMPLETE, OK, OK_INCOMPLETE, INFO, INFO_INCOMPLETE, NOTICE, NOTICE_INCOMPLETE);

    }

    static protected ProblemState[] makeStates(ProblemState... problem_states) {
        return problem_states;
    }

    static public ProblemState getProblemState(CallContext context, int index, boolean information_complete) {
        switch (index) {
            case IDLE_INDEX            : return (information_complete ? IDLE : IDLE_INCOMPLETE);
            case OK_INDEX              : return (information_complete ? OK : OK_INCOMPLETE);
            case INFO_INDEX            : return (information_complete ? INFO : INFO_INCOMPLETE);
            case NOTICE_INDEX          : return (information_complete ? NOTICE : NOTICE_INCOMPLETE);
            case UNKNOWN_INDEX         : return (information_complete ? UNKNOWN : UNKNOWN_INCOMPLETE);
            case CAUTION_INDEX         : return (information_complete ? CAUTION : CAUTION_INCOMPLETE);
            case WARNING_INDEX         : return (information_complete ? WARNING : WARNING_INCOMPLETE);
            case SEVERE_WARNING_INDEX  : return (information_complete ? SEVERE_WARNING : SEVERE_WARNING_INCOMPLETE);
            case ERROR_INDEX           : return (information_complete ? ERROR : ERROR_INCOMPLETE);
            case CRITICAL_ERROR_INDEX  : return (information_complete ? CRITICAL_ERROR : CRITICAL_ERROR_INCOMPLETE);
            case FATAL_ERROR_INDEX     : return (information_complete ? FATAL_ERROR : FATAL_ERROR_INCOMPLETE);
            case EMERGENCY_INDEX       : return (information_complete ? EMERGENCY : EMERGENCY_INCOMPLETE);
            case PANIC_INDEX           : return (information_complete ? PANIC : PANIC_INCOMPLETE);
            default                    : return null;
        }
    }

    static public ProblemState getProblemState(CallContext context, String id) {
        return state_map.get(id);
    }

    public boolean isOk(CallContext cc) {
        return (index == OK_INDEX && information_complete ? true : false);
    }

    public boolean isGreen(CallContext cc) {
        return (index < UNKNOWN_INDEX ? true : false);
    }

    public boolean isYellow(CallContext cc) {
        return ((index >= UNKNOWN_INDEX && index < ERROR_INDEX) ? true : false);
    }

    public boolean isRed(CallContext cc) {
        return (index >= ERROR_INDEX ? true : false);
    }

    public boolean isBelow(CallContext cc, ProblemState other) {
        return (index < other.index ? true : false);
    }

    public boolean isBelowOrEquals(CallContext cc, ProblemState other) {
        return (index <= other.index ? true : false);
    }

    public ProblemState combineWith(CallContext context, ProblemState other) {
        return getProblemState(context, this.index > other.index ? this.index : other.index, this.information_complete && other.information_complete);
    }

    static public ProblemState combine(CallContext context, ProblemState... problem_states) {
        ProblemState combined_problem_state = ProblemState.IDLE;
        for (ProblemState problem_state : problem_states) {
            combined_problem_state = combined_problem_state.combineWith(context, problem_state);
        }
        return combined_problem_state;
    }

    static public ProblemState combine(CallContext context, Vector<ProblemState> problem_states) {
        ProblemState[] psa = new ProblemState[0];
        if (problem_states != null) {
            psa = problem_states.toArray(psa);
        }
        return combine(context, psa);
    }

    static public ProblemState combineStatuses(CallContext context, ProblemStatus... problem_statuses) {
        ProblemState combined_problem_state = ProblemState.IDLE;
        for (ProblemStatus problem_status : problem_statuses) {
            combined_problem_state = combined_problem_state.combineWith(context, problem_status.getProblemState(context));
        }
        return combined_problem_state;
    }

    static public ProblemState combineStatuses(CallContext context, Vector<ProblemStatus> problem_statuses) {
        ProblemStatus[] psa = new ProblemStatus[0];
        if (problem_statuses != null) {
            psa = problem_statuses.toArray(psa);
        }
        return combineStatuses(context, psa);
    }

    public String toString() {
        switch (index) {
            case IDLE_INDEX            : return "idle" + (information_complete ? "" : " (incomplete)");
            case OK_INDEX              : return "ok" + (information_complete ? "" : " (incomplete)");
            case INFO_INDEX            : return "info" + (information_complete ? "" : " (incomplete)");
            case NOTICE_INDEX          : return "notice" + (information_complete ? "" : " (incomplete)");
            case UNKNOWN_INDEX         : return "unknown" + (information_complete ? "" : " (incomplete)");
            case CAUTION_INDEX         : return "caution" + (information_complete ? "" : " (incomplete)");
            case WARNING_INDEX         : return "warning" + (information_complete ? "" : " (incomplete)");
            case SEVERE_WARNING_INDEX  : return "severe warning" + (information_complete ? "" : " (incomplete)");
            case ERROR_INDEX           : return "error" + (information_complete ? "" : " (incomplete)");
            case CRITICAL_ERROR_INDEX  : return "critical error" + (information_complete ? "" : " (incomplete)");
            case FATAL_ERROR_INDEX     : return "fatal error" + (information_complete ? "" : " (incomplete)");
            case EMERGENCY_INDEX       : return "emergency" + (information_complete ? "" : " (incomplete)");
            case PANIC_INDEX           : return "panic" + (information_complete ? "" : " (incomplete)");
            default                    : return "[?]";
        }
    }

    public String getMainStateText(CallContext context) {
        switch (index) {
            case IDLE_INDEX            : return "idle";
            case OK_INDEX              : return "ok";
            case INFO_INDEX            : return "info";
            case NOTICE_INDEX          : return "notice";
            case UNKNOWN_INDEX         : return "unknown";
            case CAUTION_INDEX         : return "caution";
            case WARNING_INDEX         : return "warning";
            case SEVERE_WARNING_INDEX  : return "severe warning";
            case ERROR_INDEX           : return "error";
            case CRITICAL_ERROR_INDEX  : return "critical error";
            case FATAL_ERROR_INDEX     : return "fatal error";
            case EMERGENCY_INDEX       : return "emergency";
            case PANIC_INDEX           : return "panic";
            default                    : return "[?]";
        }
    }

    public String getCompletionStateText(CallContext context) {
        return (information_complete ? "complete" : "incomplete");
    }

    public String getShortText(CallContext context) {
        switch (index) {
            case IDLE_INDEX            : return "idle" + (information_complete ? "" : " ...");
            case OK_INDEX              : return "ok" + (information_complete ? "" : " ...");
            case INFO_INDEX            : return "info" + (information_complete ? "" : " ...");
            case NOTICE_INDEX          : return "notice" + (information_complete ? "" : " ...");
            case UNKNOWN_INDEX         : return "unknown" + (information_complete ? "" : " ...");
            case CAUTION_INDEX         : return "caution" + (information_complete ? "" : " ...");
            case WARNING_INDEX         : return "warning" + (information_complete ? "" : " ...");
            case SEVERE_WARNING_INDEX  : return "severe" + (information_complete ? "" : " ...");
            case ERROR_INDEX           : return "error" + (information_complete ? "" : " ...");
            case CRITICAL_ERROR_INDEX  : return "critical" + (information_complete ? "" : " ...");
            case FATAL_ERROR_INDEX     : return "fatal" + (information_complete ? "" : " ...");
            case EMERGENCY_INDEX       : return "emergency" + (information_complete ? "" : " ...");
            case PANIC_INDEX           : return "panic" + (information_complete ? "" : " ...");
            default                    : return "[?]";
        }
    }

    public String getUniqueIdentifier(CallContext context) {
        switch (index) {
            case IDLE_INDEX            : return "idle" + (information_complete ? "" : "-incomplete");
            case OK_INDEX              : return "ok" + (information_complete ? "" : "-incomplete");
            case INFO_INDEX            : return "info" + (information_complete ? "" : "-incomplete");
            case NOTICE_INDEX          : return "notice" + (information_complete ? "" : "-incomplete");
            case UNKNOWN_INDEX         : return "unknown" + (information_complete ? "" : "-incomplete");
            case CAUTION_INDEX         : return "caution" + (information_complete ? "" : "-incomplete");
            case WARNING_INDEX         : return "warning" + (information_complete ? "" : "-incomplete");
            case SEVERE_WARNING_INDEX  : return "severe-warning" + (information_complete ? "" : "-incomplete");
            case ERROR_INDEX           : return "error" + (information_complete ? "" : "-incomplete");
            case CRITICAL_ERROR_INDEX  : return "critical-error" + (information_complete ? "" : "-incomplete");
            case FATAL_ERROR_INDEX     : return "fatal-error" + (information_complete ? "" : "-incomplete");
            case EMERGENCY_INDEX       : return "emergency" + (information_complete ? "" : "-incomplete");
            case PANIC_INDEX           : return "panic" + (information_complete ? "" : "-incomplete");
            default                    : return "[?]";
        }
    }

    static protected Vector<Object> choice_set;

    static public Vector<Object> getChoiceSet(CallContext context) {

        if (choice_set == null) {
            choice_set = new Vector<Object>();

            choice_set.add(IDLE);
            choice_set.add(IDLE_INCOMPLETE);
            choice_set.add(OK);
            choice_set.add(OK_INCOMPLETE);
            choice_set.add(INFO);
            choice_set.add(INFO_INCOMPLETE);
            choice_set.add(NOTICE);
            choice_set.add(NOTICE_INCOMPLETE);
            choice_set.add(UNKNOWN);
            choice_set.add(UNKNOWN_INCOMPLETE);
            choice_set.add(CAUTION);
            choice_set.add(CAUTION_INCOMPLETE);
            choice_set.add(WARNING);
            choice_set.add(WARNING_INCOMPLETE);
            choice_set.add(SEVERE_WARNING);
            choice_set.add(SEVERE_WARNING_INCOMPLETE);
            choice_set.add(ERROR);
            choice_set.add(ERROR_INCOMPLETE);
            choice_set.add(CRITICAL_ERROR);
            choice_set.add(CRITICAL_ERROR_INCOMPLETE);
            choice_set.add(FATAL_ERROR);
            choice_set.add(FATAL_ERROR_INCOMPLETE);
            choice_set.add(EMERGENCY);
            choice_set.add(EMERGENCY_INCOMPLETE);
            choice_set.add(PANIC);
            choice_set.add(PANIC_INCOMPLETE);
        }

        return choice_set;
    }

    static protected Map<String,Vector<UIEquipment>> ui_equipment_map;

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        if (ui_equipment_map == null) {
            ui_equipment_map = (Map<String,Vector<UIEquipment>>) com.sphenon.engines.aggregator.Aggregator.create(context, "com/sphenon/basics/monitoring/ProblemState_UIEquipments");
        }
        return ui_equipment_map == null ? null : ui_equipment_map.get(getUniqueIdentifier(context));
    }

    public Vector<UIEquipment> getUIEquipments(CallContext context, String feature) {
        return null;
    }
}
