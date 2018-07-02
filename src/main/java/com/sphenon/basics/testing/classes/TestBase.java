package com.sphenon.basics.testing.classes;

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
import com.sphenon.ui.annotations.*;

import com.sphenon.basics.testing.*;

abstract public class TestBase implements Test {

    protected String id;

    public String getId (CallContext context) {
        return this.id;
    }

    public String defaultId (CallContext context) {
        return null;
    }

    public void setId (CallContext context, String id) {
        this.id = id;
    }

    protected String path;

    public String getPath (CallContext context) {
        return this.path;
    }

    public String defaultPath (CallContext context) {
        return null;
    }

    public void setPath (CallContext context, String path) {
        this.path = path;
    }
}
