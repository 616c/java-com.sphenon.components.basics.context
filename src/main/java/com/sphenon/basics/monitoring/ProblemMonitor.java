package com.sphenon.basics.monitoring;

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

import java.util.Vector;

/**
   An object which is capable of registering {@link ProblemStatus} instances
   and reporting them.
*/
public interface ProblemMonitor extends MonitorableObject {

    public void addProblemStatus(CallContext context, ProblemStatus problem_status);

    public void addProblemStatus(CallContext context, ProblemState problem_state, Problem problem);

    public void addProblemStatus(CallContext context, ProblemState problem_state, String message);

    public void addProblemStatus(CallContext context, ProblemState problem_state, Throwable exception);

    public void addProblemStatus(CallContext context, ProblemState problem_state, int return_code);
}