package com.sphenon.basics.system;

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

import java.io.PrintStream;
import java.io.InputStream;

public class SystemContext extends SpecificContext {

    static protected SystemContext default_singleton;
    protected boolean is_default_singelton;

    static public SystemContext getOrCreate(Context context) {
        SystemContext system_context = (SystemContext) context.getSpecificContext(SystemContext.class);
        if (system_context == null) {
            system_context = new SystemContext(context, false);
            context.setSpecificContext(SystemContext.class, system_context);
        }
        return system_context;
    }

    static public SystemContext get(Context context) {
        SystemContext system_context = (SystemContext) context.getSpecificContext(SystemContext.class);
        if (system_context != null) {
            return system_context;
        }
        return default_singleton == null ? (default_singleton = new SystemContext(context, true)) : default_singleton;
    }

    static public SystemContext create(Context context) {
        SystemContext system_context = new SystemContext(context, false);
        context.setSpecificContext(SystemContext.class, system_context);
        return system_context;
    }

    protected SystemContext (Context context, boolean is_default_singelton) {
        super(context);
        this.is_default_singelton = is_default_singelton;
    }

    protected PrintStream error_stream;

    public void setErrorStream(CallContext context, PrintStream error_stream) {
        this.error_stream = error_stream;
    }

    public PrintStream getErrorStream(CallContext cc) {
        SystemContext system_context;
        return (this.error_stream != null ?
                     this.error_stream
                  : (system_context = (SystemContext) this.getCallContext(SystemContext.class)) != null ?
                       system_context.getErrorStream(cc)
                     : System.err
               );
    }

    static public class err {
        static public void println(CallContext context, String string) {
            SystemContext.get((Context) context).getErrorStream(context).println(string);
        }
        static public PrintStream stream(CallContext context) {
            return SystemContext.get((Context) context).getErrorStream(context);
        }
    }

    protected PrintStream output_stream;

    public void setOutputStream(CallContext context, PrintStream output_stream) {
        this.output_stream = output_stream;
    }

    public PrintStream getOutputStream(CallContext cc) {
        SystemContext system_context;
        return (this.output_stream != null ?
                     this.output_stream
                  : (system_context = (SystemContext) this.getCallContext(SystemContext.class)) != null ?
                       system_context.getOutputStream(cc)
                     : System.out
               );
    }

    static public class out {
        static public void println(CallContext context, String string) {
            SystemContext.get((Context) context).getOutputStream(context).println(string);
        }
        static public PrintStream stream(CallContext context) {
            return SystemContext.get((Context) context).getOutputStream(context);
        }
    }

    protected InputStream input_stream;

    public void setInputStream(CallContext context, InputStream input_stream) {
        this.input_stream = input_stream;
    }

    public InputStream getInputStream(CallContext cc) {
        SystemContext system_context;
        return (this.input_stream != null ?
                     this.input_stream
                  : (system_context = (SystemContext) this.getCallContext(SystemContext.class)) != null ?
                       system_context.getInputStream(cc)
                     : System.in
               );
    }
}
