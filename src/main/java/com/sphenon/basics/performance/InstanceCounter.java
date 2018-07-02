package com.sphenon.basics.performance;

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
import com.sphenon.basics.context.classes.*;

public class InstanceCounter {
    protected String fullclassname;

    public long created         = 0;
    public long finalized       = 0;
    public long created_last    = 0;
    public long finalized_last  = 0;

    static protected InstanceCounter first_counter;
           protected InstanceCounter next_counter;
    static protected InstanceCounter last_counter;

    public InstanceCounter (CallContext call_context, String fullclassname) {
        Context context = Context.create(call_context);
        this.fullclassname = fullclassname;

        synchronized (InstanceCounter.class) {
            if (first_counter == null) {
                first_counter = this;
                next_counter = null;
                last_counter = this;
            } else {
                last_counter.next_counter = this;
                next_counter = null;
            }
        }
    }

    public void notifyInstanceCreation (CallContext call_context) {
        created++;
    }

    public void notifyInstanceFinalization (CallContext call_context) {
        finalized++;
    }

    static public InstanceCounter getFirst(CallContext context) {
        return first_counter;
    }

    public InstanceCounter getNext(CallContext context) {
        return next_counter;
    }

    public String getFullClassName(CallContext context) {
        return fullclassname;
    }
}
