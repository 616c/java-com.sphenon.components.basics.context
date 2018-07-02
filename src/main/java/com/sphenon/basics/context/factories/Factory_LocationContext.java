package com.sphenon.basics.context.factories;

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

public class Factory_LocationContext {

    public void Factory_LocationContext(CallContext context) {
    }

    protected LocationContext location_context;

    public LocationContext precreate (CallContext context) {
        if (this.location_context == null) {
            this.location_context = RootContext.createLocationContext();
        }
        return this.location_context;
    }

    public LocationContext create(CallContext context) {
        LocationContext result = this.precreate(context);
        this.location_context = null;
        return result;
    }

    // this attribute serves the sole purpose to have
    // a "holder" in ocps for specific contexts
    // this is since the connection from locationcontext
    // to specific ones can only be made during construction
    // so there's the need to pass the locationcontext
    // via OID into the specific ones, but then there is
    // primarily no place to put the specific ones

    protected SpecificContext[] specific_contexts;

    public SpecificContext[] getSpecificContexts (CallContext context) {
        return this.specific_contexts;
    }

    public SpecificContext[] defaultSpecificContexts (CallContext context) {
        return null;
    }

    public void setSpecificContexts (CallContext context, SpecificContext[] specific_contexts) {
        this.specific_contexts = specific_contexts;
    }
}
