package com.sphenon.basics.context.classes;

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

public class LocatedClass implements Located {

    protected LocationContext location_context;

    public LocationContext getLocationContext (CallContext context) {
        if (this.location_context == null) {
            this.location_context = RootContext.createLocationContext();
        }
        return this.location_context;
    }

    public LocationContext defaultLocationContext (CallContext context) {
        return null;
    }

    public void setLocationContext (LocationContext location_context) {
        this.location_context = location_context;
    }

}
