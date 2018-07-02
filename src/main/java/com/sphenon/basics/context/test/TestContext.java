package com.sphenon.basics.context.test;

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
// import com.sphenon.basics.context.tplinst.*;

public class TestContext extends SpecificContext {

    static public TestContext get(Context context) {
        return (TestContext) context.getSpecificContext(TestContext.class);
    }

    static public TestContext create(Context context) {
        TestContext tc = new TestContext(context);
        context.setSpecificContext(TestContext.class, tc);
        return tc;
    }

    protected TestContext (Context context) {
        super(context);
        this.test_value = null;
    }

    private String test_value;

    public void setTestValue(String test_value) {
        if (this.test_value == null) {
            this.test_value = new String();
        }
        this.test_value = test_value;
    }

    public String getTestValue() {
        TestContext tc;
        return (this.test_value != null ?
                     this.test_value
                  : (tc = (TestContext) this.getCallContext(TestContext.class)) != null ?
                       tc.getTestValue()
                     : "default"
               );
    }
}
