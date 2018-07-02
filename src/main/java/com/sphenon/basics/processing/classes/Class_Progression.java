package com.sphenon.basics.processing.classes;

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
import com.sphenon.basics.debug.*;

import com.sphenon.basics.processing.*;

import com.sphenon.engines.aggregator.annotations.*;

/**
   Simple progression.
*/
public class Class_Progression implements Progression, Dumpable, ContextAware {

    static public Progression NO_PROGRESS       = new Class_Progression(null, Progress.NO_PROGRESS);
    static public Progression POSSIBLY_PROGRESS = new Class_Progression(null, Progress.POSSIBLY_PROGRESS);
    static public Progression SOME_PROGRESS     = new Class_Progression(null, Progress.SOME_PROGRESS);
    static public Progression SKIPPED           = new Class_Progression(null, Progress.SKIPPED);
    static public Progression COMPLETED         = new Class_Progression(null, Progress.COMPLETED);

    protected Progress progress;

    public Class_Progression(CallContext context) {
        this.progress = Progress.NO_PROGRESS;
    }

    public Class_Progression(CallContext context, Progress progress) {
        this.progress = progress;
    }

    protected Class_Progression(CallContext context, Progress progress, Float percent) {
        this.progress = progress;
        this.percent = percent;
    }

    public Progress getProgress(CallContext context) {
        return this.progress;
    }

    public void setProgress(CallContext context, Progress progress) {
        this.progress = progress;
    }

    protected Float percent;

    public float getPercent(CallContext context) {
        if (this.percent != null) {
            return this.percent;
        } else {
            switch(progress) {
                case NO_PROGRESS: return 0;
                case POSSIBLY_PROGRESS: return 1;
                case SOME_PROGRESS: return 10;
                case SKIPPED: return 100;
                case COMPLETED: return 100;
                default: return 0;
            }
        }
    }

    @OCPOptional()
    public void setPercent(CallContext context, float percent) {
        this.setPercent(context, percent, false);
    }

    public void setPercent(CallContext context, float percent, boolean adjust_progress) {
        this.percent = percent;
        if (this.percent == 100.0) {
            this.progress = Progress.COMPLETED;
        } else if (this.percent > 0.0) {
            this.progress = Progress.SOME_PROGRESS;
        } else {
            this.progress = Progress.NO_PROGRESS;
        }
    }

    public Progression getSnapshot(CallContext context) {
        return new Class_Progression(context, this.progress, this.percent);
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, progress.toString() + '/' + getPercent(context) + '%');
    }

    public boolean equals(Object object) {
        if (    object == null
             || (object instanceof Progression) == false
           ) { return false; }
        CallContext context = RootContext.getFallbackCallContext();
        return (   (   (    this.progress == null
                         && ((Progression) object).getProgress(context) == null
                       )
                    || (    this.progress != null
                         && this.progress.equals(((Progression) object).getProgress(context))
                       )
                   )
                && (   this.getPercent(context) == (((Progression) object).getPercent(context))
                   )
               );
    }

    public String toString(CallContext context) {
        return progress.toString() + '/' + getPercent(context) + '%';
    }
}
