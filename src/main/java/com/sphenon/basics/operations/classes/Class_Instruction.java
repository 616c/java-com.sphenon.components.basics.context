package com.sphenon.basics.operations.classes;

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
import com.sphenon.basics.monitoring.*;

import com.sphenon.basics.operations.*;

public class Class_Instruction implements Instruction, Dumpable {

    public Class_Instruction (CallContext context) {
    }

    public Class_Instruction (CallContext context, String description) {
        this.description = description;
    }

    protected String description;

    public String getDescription (CallContext context) {
        return this.description;
    }

    public void setDescription (CallContext context, String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Instruction", this.description);
    }
}
