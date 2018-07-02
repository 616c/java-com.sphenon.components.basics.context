package com.sphenon.basics.operations;

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
import com.sphenon.basics.processing.*;

import com.sphenon.ui.annotations.*;

@UIId         ("execution")
@UIIdIndex    (true)
@UIName       ("Execution")
@UIClassifier ("Execution")
@UIState      ("js:instance.getProblemState(context)")
@UIEvents     ("ProcessingEvent")
public interface Execution {
    /**
       Details about what is executed.
    */
    public Instruction getInstruction (CallContext context);

    /**
       The current {@link ProblemState} of the execution.
    */
    public ProblemState getProblemState (CallContext context);

    /**
       Details about the {@link ProblemState}.
    */
    public Problem getProblem (CallContext context);

    /**
       The current {@link ActivityState} of the execution.
    */
    public ActivityState getActivityState (CallContext context);

    /**
       Details about the {@link Progression} of the execution.
    */
    public Progression getProgression (CallContext context);

    /**
       Detailed, human readable {@link Record} of the execution.
    */
    public Record getRecord (CallContext context);

    /**
       {@link Performance} data of the execution.
    */
    public Performance getPerformance (CallContext context);

    // Siehe auch work.xuxf [EXEC]
    /**
       Synchronises with this execution.
       Waits until it's activity state is either ABORTED or COMPLETED.
       After return, you will usually check problem state, activity state
       and progression.

       @return this instance, for convenience
    */
    public Execution wait (CallContext context);
}
