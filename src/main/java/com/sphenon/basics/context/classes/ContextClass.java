package com.sphenon.basics.context.classes;

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
import java.lang.reflect.*;

public class ContextClass extends Context {
    private Context call_context;
    private Context location_context;
    
    static volatile private ContextClass singleton;

    static protected ContextClass getSingleton() {
        if (singleton == null) {
            synchronized(ContextClass.class) {
                if (singleton == null) {
                    singleton = new ContextClass();
                }
            }
        }
        return singleton;
    }
    
    static public LocationContext createLocationContext() {
        return new ContextClass();
    }

    protected ContextClass () {
        this.call_context = null;
        this.location_context = null;
        this.call_depth = 0;
    }

    public ContextClass (Context call_context) {
        if (call_context == null) {
            throw new RuntimeException("precondition violation in context: CallContext is (null)");
        }
        this.call_context = call_context;
        this.location_context = null;
        this.call_depth = call_context.getCallDepth() + 1;
    }

    public ContextClass (Context call_context, Context location_context) {
        this.call_context = call_context;
        this.location_context = location_context;
        this.call_depth = (call_context == null ? 0 : call_context.getCallDepth() + 1);
    }

    protected int call_depth;
    public int getCallDepth() {
        return call_depth;
    }

    //-----------------------------------------------------------------------
    private java.util.Map context_map;

    {   context_map = null; // new java.util.Hashtable();
    }

    /**
       For debugging purposes only - not intended for direct modification.
    */
    public java.util.Map getContextMap() {
        return context_map;
    }

    public SpecificContext getSpecificContext(Class reg_class) {
        return getSpecificContext(reg_class, false);
    }

    public SpecificContext getSpecificContext(Class reg_class, boolean only_local) {
        SpecificContext sc = null;
        if (context_map != null) {
            sc = (SpecificContext) context_map.get(reg_class);
        }

        if (sc == null && ! only_local) {
            SpecificContext slc = (this.location_context != null ? this.location_context.getSpecificContext(reg_class) : null);
            SpecificContext scc = (this.call_context     != null ? this.call_context.getSpecificContext(reg_class) : null);
            if (slc != null && scc != null) {
                // in case we got both context (call and location) we need a local,
                // combining specific instance, since otherwise property lookup
                // may miss values

                try {
                    sc = (SpecificContext) reg_class.getMethod("create", Context.class).invoke(null, this);
                } catch (NoSuchMethodException nsme) {
                    throw new Error("Specific context " + reg_class.getName() + " contains no appropriate create method");
                } catch (IllegalAccessException iae) {
                    throw new Error("Specific context " + reg_class.getName() + ": invocation of create method failed", iae);
                } catch (IllegalArgumentException iae) {
                    throw new Error("Specific context " + reg_class.getName() + ": invocation of create method failed", iae);
                } catch (InvocationTargetException ite) {
                    throw new Error("Specific context " + reg_class.getName() + ": invocation of create method failed", ite);
                } catch (ClassCastException cce) {
                    throw new Error("Specific context " + reg_class.getName() + ": create method returned not a SpecificContext", cce);
                }

                this.setSpecificContext(reg_class, sc);
            } else {
                if (slc != null) { sc = slc; }
                if (scc != null) { sc = scc; }
            }
        }
        return sc;
    }

    public SpecificContext getSpecificCallContext(Class reg_class) {
        if (this.call_context != null) {
            return this.call_context.getSpecificContext(reg_class);
        }
        return null;
    }

    public SpecificContext getSpecificLocationContext(Class reg_class) {
        if (this.location_context != null) {
            return this.location_context.getSpecificContext(reg_class);
        }
        return null;
    }

    public void setSpecificContext(Class reg_class, SpecificContext specific_context) {
        if (context_map == null) {
            context_map = new java.util.concurrent.ConcurrentHashMap(4);
        }
        context_map.put(reg_class, specific_context);
    }

    public CallContext getCallContext() {
        return this.call_context;
    }

    public LocationContext getLocationContext() {
        return this.location_context;
    }
    /**
     * Debug Ausgabe
     * Wen's beisst der nehme es raus
     *
     * AL: <naseruempf>naja<(naseruempf>
     */
    public String toString() {
        boolean session=false;
        boolean application=false;
        String out=super.toString() + '[';
        String sep = "";
        if (context_map != null) {
            for (Object key : context_map.keySet()) {
                if (key.toString().contains("SessionContext")) {
                    session = true;
                }
                if (key.toString().contains("ApplicationContext")) {
                    application = true;
                }
            }
        }
        ContextClass parent = (ContextClass) call_context;
        while (parent != null) {
            if (parent.context_map != null) {
                for (Object key : parent.context_map.keySet()) {
                    if (key.toString().contains("SessionContext")) {
                        session = true;
                    }
                    if (key.toString().contains("ApplicationContext")) {
                        application = true;
                    }
                }
            }
            parent = (ContextClass)parent.call_context;
        }
        if (session) { 
            out += sep + "Session";
            sep = ",";
        }
        if (application) { 
            out += sep + "Application";
            sep = ",";
        }
        out=out + "]";
        return out;
    }
}
