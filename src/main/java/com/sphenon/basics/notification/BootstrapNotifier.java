package com.sphenon.basics.notification;

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

public class BootstrapNotifier {
    static protected BootstrapNotifier bootstrap_notifier;

    static protected boolean configured    = false;
    static protected boolean checkpoint_on = false;
    static protected boolean trace_on      = false;

    static protected StringBuilder pre_configuration_checkpoint;
    static protected StringBuilder pre_configuration_trace;

    public BootstrapNotifier (CallContext context) {
    }

    static protected BootstrapNotifier get(CallContext context) {
        if (bootstrap_notifier == null) {
            bootstrap_notifier = new BootstrapNotifier(context);
        }
        return bootstrap_notifier;
    }

    public void do_sendCheckpoint (CallContext context, String message) {
        System.err.println(message);
    }

    public void do_sendTrace (CallContext context, String message) {
        System.err.println(message);
    }

    static public void sendCheckpoint (CallContext context, String message) {
        if (checkpoint_on) {
            get(context).do_sendCheckpoint(context, message);
        } else if (configured == false) {
            if (pre_configuration_checkpoint == null) {
                pre_configuration_checkpoint = new StringBuilder();
            }
            pre_configuration_checkpoint.append(message).append('\n');
        }
    }

    static public void sendTrace (CallContext context, String message) {
        if (trace_on) {
            get(context).do_sendTrace(context, message);
        } else if (configured == false) {
            if (pre_configuration_trace == null) {
                pre_configuration_trace = new StringBuilder();
            }
            pre_configuration_trace.append(message).append('\n');
        }
    }

    static public void configure(CallContext context, boolean checkpoint, boolean trace) {
        checkpoint_on = checkpoint;
        trace_on = trace;
        configured = true;
        if (pre_configuration_checkpoint != null) {
            if (checkpoint_on) {
                get(context).do_sendCheckpoint(context, pre_configuration_checkpoint.toString());
            }
            pre_configuration_checkpoint = null;
        }
        if (pre_configuration_trace != null) {
            if (trace_on) {
                get(context).do_sendTrace(context, pre_configuration_trace.toString());
            }
            pre_configuration_trace = null;
        }
    }
}
