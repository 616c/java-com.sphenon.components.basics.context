package com.sphenon.basics.testing;

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

@UIId         ("js:instance.getId(context)")
@UIName       ("js:instance.getId(context)")
@UIClassifier ("Test")
public interface Test {

    public String getId(CallContext context);
    public String getPath(CallContext context);

    @UIOperation(
        Classifier="Test",
        Action="js:instance.perform(context, new Packages.com.sphenon.basics.testing.classes.ClassTestRun(context))",
        Result=true
    )
    public TestResult perform (CallContext context, TestRun test_run);
}
