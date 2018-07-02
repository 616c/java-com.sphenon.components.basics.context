package com.sphenon.basics.operations;

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
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;

public class ContinuationOption {

    static final public ContinuationOption STEP_OVER = new ContinuationOption(null, "Step over", "o");
    static final public ContinuationOption STEP_INTO = new ContinuationOption(null, "Step into", "i");
    static final public ContinuationOption SKIP_STEP = new ContinuationOption(null, "Skip step", "s");
    static final public ContinuationOption PROCEED   = new ContinuationOption(null, "Proceed without further interruption", "p");
    static final public ContinuationOption CANCEL    = new ContinuationOption(null, "Cancel execution [c]", "c");

    public ContinuationOption(CallContext context, String description, String keys) {
        this.description = description;
        this.keys = keys;
    }

    protected String description;

    public String getDescription (CallContext context) {
        return this.description;
    }

    protected String keys;

    public String getKeys (CallContext context) {
        return this.keys;
    }

    public boolean matchesKey (CallContext context, Character c) {
        return c.toString().matches(this.keys);
    }
}
