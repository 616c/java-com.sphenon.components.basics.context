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

/**
   Reflects a group of progressions, like for a sequence of executions.
*/
public class ProgressionGroup implements Progression, Dumpable {

    protected Progression[] progressions;
    protected boolean alternatives;

    public ProgressionGroup(CallContext context, Progression... progressions) {
        this(context, false, null, progressions);
    }

    public ProgressionGroup(CallContext context, boolean alternatives, Progression... progressions) {
        this(context, alternatives, null, progressions);
    }

    public ProgressionGroup(CallContext context, float[] weights, Progression... progressions) {
        this(context, false, weights, progressions);
    }

    public ProgressionGroup(CallContext context, boolean alternatives, float[] weights, Progression... progressions) {
        this.progressions = progressions;
        this.weights = weights;
        this.alternatives = alternatives;
    }

    protected float[] weights;

    public float[] getWeights (CallContext context) {
        return this.weights;
    }

    public void setWeights (CallContext context, float[] weights) {
        this.weights = weights;
    }

    public Progress getProgress(CallContext context) {
        if (this.alternatives) {
            boolean possibly_progress = false;
            boolean some_progress     = false;
            boolean one_completed     = false;
            if (progressions != null && progressions.length != 0) {
                for (Progression progression : progressions) {
                    if (progression != null) {
                        Progress progress = progression.getProgress(context);
                        if (progress != null) {
                            switch (progress) {
                                case NO_PROGRESS :
                                    break;
                                case POSSIBLY_PROGRESS :
                                    possibly_progress = true;
                                    break;
                                case SOME_PROGRESS :
                                    some_progress     = true;
                                    break;
                                case COMPLETED :
                                    one_completed     = true;
                                    break;
                            }
                        }
                    }
                }
            }
        if (one_completed)     { return Progress.COMPLETED; }
        if (some_progress)     { return Progress.SOME_PROGRESS; }
        if (possibly_progress) { return Progress.POSSIBLY_PROGRESS; }
        return Progress.NO_PROGRESS;
        } else {
            boolean possibly_progress = false;
            boolean some_progress     = false;
            boolean all_completed     = true;
            if (progressions != null && progressions.length != 0) {
                for (Progression progression : progressions) {
                    if (progression == null) {
                    } else {
                        Progress progress = progression.getProgress(context);
                        if (progress == null) {
                            all_completed     = false;
                        } else {
                            switch (progress) {
                                case NO_PROGRESS :
                                    all_completed     = false;
                                    break;
                                case POSSIBLY_PROGRESS :
                                    possibly_progress = true;
                                    all_completed     = false;
                                    break;
                                case SOME_PROGRESS :
                                    some_progress     = true;
                                    all_completed     = false;
                                    break;
                                case COMPLETED :
                                    break;
                            }
                        }
                    }
                }
            }
            if (all_completed)     { return Progress.COMPLETED; }
            if (some_progress)     { return Progress.SOME_PROGRESS; }
            if (possibly_progress) { return Progress.POSSIBLY_PROGRESS; }
            return Progress.NO_PROGRESS;
        }
    }

    static protected int indent = 0;
    static public boolean dumpcalc = false;

    public float getPercent(CallContext context) {
        indent++;
        float percent = 0;
        int j;
        boolean first = true;
        if (progressions != null && progressions.length != 0) {
            float[] w = this.weights;
            if (w == null) {
                w = new float[progressions.length];
                float defw = 1.0F / ((float) progressions.length);
                for (int i=0; i<progressions.length; i++) {
                    w[i] = defw;
                }
            } else if (w.length < progressions.length) {
                float[] xw = new float[progressions.length];
                float defw = 1.0F / ((float) progressions.length);
                float scale = w.length / ((float) progressions.length);
                for (int i=0; i<progressions.length; i++) {
                    if (i < w.length) {
                        xw[i] = w[i] * scale;
                    } else {
                        xw[i] = defw;
                    }
                }
                w = xw;
            }
            int i=0;
            for (Progression progression : progressions) {
                if (progression == null) {
                } else {
                    float p = progression.getPercent(context);
                    if (dumpcalc) {
                        StringBuilder sb = new StringBuilder();
                        for (j=0; j<indent; j++) { sb.append("  "); }
                        sb.append((first ? "[   " : "  + ") + p + " * " + w[i]);
                        first = false;
                        System.err.println(sb.toString());
                    }
                    percent += p * w[i++];
                }
            }
        } else {
            percent = 100.0F;
        }
        if (dumpcalc) {
            StringBuilder sb = new StringBuilder();
            for (j=0; j<indent; j++) { sb.append("  "); }
            sb.append((first ? "[ = " : "  = ") + percent + "]");
            first = false;
            System.err.println(sb.toString());
        }
        indent--;
        return percent;
    }

    public Progression getSnapshot(CallContext context) {
        return new Class_Progression(context, this.getProgress(context), this.getPercent(context));
    }

    public boolean equals(Object object) {
        if (    object == null
             || (object instanceof Progression) == false
           ) { return false; }
        CallContext context = RootContext.getFallbackCallContext();
        Progress progress = this.getProgress(context);
        return (   (   (    progress == null
                         && ((Progression) object).getProgress(context) == null
                       )
                    || (    progress != null
                         && progress.equals(((Progression) object).getProgress(context))
                       )
                   )
                && (   this.getPercent(context) == (((Progression) object).getPercent(context))
                   )
               );
    }

    public void dump(CallContext context, DumpNode dump_node) {
        DumpNode dn = dump_node.openDump(context, "Progressions");
        int i = 1;
        for (Progression progression : progressions) {
            dn.dump(context, (new Integer(i++)).toString(), progression);
        }
    }
}
