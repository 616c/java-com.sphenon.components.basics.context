package com.sphenon.basics.monitoring;

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
import com.sphenon.sm.tsm.*;
import com.sphenon.ui.core.*;
import com.sphenon.ui.core.classes.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import com.sphenon.ui.annotations.*;

@UIId         ("problemcategory")
@UIName       ("ProblemCategory")
@UIClassifier ("ProblemCategory")
public enum ProblemCategory implements UIEquipped, TSMEquipped {

    UNKNOWN              (  -1,   0),
    OK                   (   0, 200), // OK

    // temporary
    BUSY                 (1000, 503), // SERVICE_UNAVAILABLE

    // client side
    INVALID_ARGUMENTS    (2000, 400), // BAD_REQUEST
    INVALID_ACTION       (2001, 400), // BAD_REQUEST
    PROTOCOL_VIOLATION   (2002, 400), // BAD_REQUEST

    // server side
    EXECUTION_FAILURE    (3000, 500), // INTERNAL_SERVER_ERROR
    CLEANUP_IRREGULARITY (3001, 500), // INTERNAL_SERVER_ERROR

    // complex/mixed
    FOLLOW_UP            (4000,   0),
    MULTIPLE             (4001,   0);

    public long code;
    public int  http_equivalent;

    ProblemCategory(long code, int http_equivalent) {
        this.code = code;
        this.http_equivalent = http_equivalent;
    }

    public int getHTTPEquivalent(CallContext context) {
        return http_equivalent;
    }

    static public ProblemCategory getByHTTPEquivalent(CallContext context, int http_equivalent) {
        for (ProblemCategory pc: ProblemCategory.class.getEnumConstants()) {
            if (pc.getHTTPEquivalent(context) == http_equivalent) {
                return pc;
            }
        }
        return null;
    }

    public ProblemCategory combineWith(CallContext context, ProblemCategory other) {
        if (this  == other                    ) { return this; }
        if (this  == ProblemCategory.MULTIPLE ) { return ProblemCategory.MULTIPLE; }
        if (other == ProblemCategory.MULTIPLE ) { return ProblemCategory.MULTIPLE; }
        if (other == ProblemCategory.FOLLOW_UP) { return this;  }
        if (this  == ProblemCategory.FOLLOW_UP) { return other; }
        if (other == ProblemCategory.OK       ) { return this;  }
        if (this  == ProblemCategory.OK       ) { return other; }
        if (other == ProblemCategory.UNKNOWN  ) { return this;  }
        if (this  == ProblemCategory.UNKNOWN  ) { return other; }
        if (other == ProblemCategory.BUSY     ) { return this;  }
        if (this  == ProblemCategory.BUSY     ) { return other; }
        return ProblemCategory.MULTIPLE;
    }

    // UI Equipped ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected Vector<UIEquipment> ui_equipments;

    public Vector<UIEquipment> getUIEquipments(CallContext context) {
        if (this.ui_equipments == null) {
            if (this.ui_equipments == null) {
                this.ui_equipments = new Vector<UIEquipment>();
                this.ui_equipments.add(new Class_UIEquipment(context, UIEquipmentType.Name, toString()));
                this.ui_equipments.add(new Class_UIEquipment(context, UIEquipmentType.Text, toString()));
            }
        }
        return this.ui_equipments;
    }

    // TSM mapping ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static public String               getPersistentTypeName     (CallContext context) {
        return "String";
    }

    static public String               convertToPersistentType   (CallContext context, ProblemCategory ad_instance) {
        return ad_instance == null ? null : ad_instance.name();
    }

    static public ProblemCategory      convertFromPersistentType (CallContext context, String          sm_instance) {
        return sm_instance == null ? null : java.lang.Enum.<ProblemCategory>valueOf(ProblemCategory.class, sm_instance);
    }

    public Object _getState(CallContext context) {
        return name();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
