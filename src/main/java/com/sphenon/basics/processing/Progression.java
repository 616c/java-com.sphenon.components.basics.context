package com.sphenon.basics.processing;

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

import com.sphenon.ui.annotations.*;

/**
   Formal represenation of the progression of an activity, e.g. an {@link Execution}.
   More or less aggregated, structured and typed (in contrast to flat and raw) and with
   a machine interpretable semantic (in contrast to plain log files, see {@link Record}).
*/
@UIId         ("progression")
@UIName       ("Progression")
@UIClassifier ("Progression")
public interface Progression {

    /**
       See {@link Progress}
    */
    public Progress getProgress(CallContext context);

    /**
       More or less accurate representation of current progress measured in percent.
     */
    public float getPercent(CallContext context);

    /**
       Creates a frozen clone of this instance, representing exactly the same
       progression state as the current. The snapshot can be used later to
       detect changes in progression by invoking the equals method.
    */
    public Progression getSnapshot(CallContext context);
}
/*
  subclasses: ProgressInPercent,
              SequenceOfSteps (d.h. Sequence), --> implemented in "work"
              GoalStack              
*/
