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

import java.io.PrintStream;

/**
   A {@link Problem} whose cause is explained.
*/
public class ProblemMessage implements Problem, Dumpable {

    public ProblemMessage(CallContext context, String message) {
        this.message = message;
    }

    protected String message;

    public String getMessage (CallContext context) {
        return this.message;
    }

    public void setMessage (CallContext context, String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Message", this.message);
    }
}
