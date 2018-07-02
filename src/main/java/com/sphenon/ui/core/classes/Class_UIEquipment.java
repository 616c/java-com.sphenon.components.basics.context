package com.sphenon.ui.core.classes;

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

public class Class_UIEquipment implements UIEquipment {

    public Class_UIEquipment(CallContext context) {
    }

    public Class_UIEquipment(CallContext context, UIEquipmentType type, Object value) {
        this.type = type;
        this.value = value;
    }

    protected UIEquipmentType type;

    public UIEquipmentType getType (CallContext context) {
        return this.type;
    }

    public void setType (CallContext context, UIEquipmentType type) {
        this.type = type;
    }

    protected Object value;

    public Object getValue (CallContext context) {
        return this.value;
    }

    public void setValue (CallContext context, Object value) {
        this.value = value;
    }
}
