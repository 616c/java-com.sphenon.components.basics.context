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

public class Class_ThreadContext implements ThreadContext {

    public Class_ThreadContext(CallContext context, ClassLoader class_loader) {
        this.context = context;
        this.class_loader = class_loader;
    }

    protected CallContext context;

    public CallContext getContext () {
        return this.context;
    }

    public void setContext (CallContext context) {
        this.context = context;
    }

    protected ClassLoader class_loader;

    public ClassLoader getClassLoader () {
        return this.class_loader;
    }

    public void setClassLoader (ClassLoader class_loader) {
        this.class_loader = class_loader;
    }
}
