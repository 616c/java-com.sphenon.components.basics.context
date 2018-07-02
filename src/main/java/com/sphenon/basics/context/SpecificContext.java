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

public class SpecificContext {
    Context         context;
    boolean         parents_retrieved;
    SpecificContext call_context;
    boolean         environment_retrieved;
    SpecificContext location_context;
    
    protected SpecificContext (Context context) {
        this.context = context;
        this.parents_retrieved = false;
        this.environment_retrieved = false;
    }

    protected SpecificContext getCallContext (Class reg_class) {
        return (parents_retrieved ?
                   this.call_context
                 : ((parents_retrieved = true) ? this.call_context = context.getSpecificCallContext(reg_class) : null)
               );
    }

    protected SpecificContext getLocationContext (Class reg_class) {
        return (environment_retrieved ?
                   this.location_context
                 : ((environment_retrieved = true) ? this.location_context = context.getSpecificLocationContext(reg_class) : null)
               );
    }
}
