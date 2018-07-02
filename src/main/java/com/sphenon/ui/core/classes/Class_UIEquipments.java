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

import java.util.*;

public class Class_UIEquipments implements UIEquipments {

    public Class_UIEquipments(CallContext context, List<UIEquipment> ui_equipments) {
        this.ui_equipments = ui_equipments;
    }

    protected List<UIEquipment> ui_equipments;

    public List<UIEquipment> getUIEquipments(CallContext context) {
        return this.ui_equipments;
    }
}
