package com.sphenon.basics.context;

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

import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.performance.*;

abstract public class Context implements CallContext, LocationContext {

    static public InstanceCounter instance_counter;

    public Context () {
        if (instance_counter != null) { instance_counter.notifyInstanceCreation(this); }
    }

    protected void finalize() throws Throwable {
        if (instance_counter != null) { instance_counter.notifyInstanceFinalization(this); }
    }

    /** Create new local context based on call context
     */
    static public Context create(CallContext cc) {
        return new ContextClass((Context) cc);
    }

    static public Context create(CallContext cc, LocationContext lc) {
        return new ContextClass((Context) cc, (Context) lc);
    }

    // from CallContext
    protected Context getContext() {
        return this;
    }

    abstract public CallContext getCallContext();

    abstract public LocationContext getLocationContext();

    abstract public int getCallDepth();

    abstract public SpecificContext getSpecificContext(Class reg_class);

    abstract public SpecificContext getSpecificContext(Class reg_class, boolean only_local);

    abstract public SpecificContext getSpecificCallContext(Class reg_class);

    abstract public SpecificContext getSpecificLocationContext(Class reg_class);

    abstract public void setSpecificContext(Class reg_class, SpecificContext specific_context);
}
