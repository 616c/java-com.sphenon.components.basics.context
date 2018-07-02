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

public class TestResult_Success implements TestResult {

    public TestResult_Success (CallContext context) {
    }

    public ProblemState getProblemState (CallContext context) {
        return ProblemState.OK;
    }
    public Problem getProblem (CallContext context) {
        return null;
    }

    public String toString() {
        return ProblemState.OK.toString().toUpperCase();
    }

}
