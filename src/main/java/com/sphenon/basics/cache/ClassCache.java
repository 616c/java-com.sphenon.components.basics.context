package com.sphenon.basics.cache;

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
import com.sphenon.basics.system.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class ClassCache {

    static protected ClassCacheData class_cache;
    static protected Set<String> class_cache_checked;

    static public boolean cache_disabled;

    static public int not_found_notice_every = 1000;
    static public int not_found_notice_maximum = 50;
    static public int not_found_dump_maximum = 10;

    static public boolean debug;

    static public void log(CallContext context, String message) {
        if (debug) { System.err.println(message); }
    }

    static public Map<String,Class> getClassCache(CallContext context, ClassLoader cl, String clid) {
        ClassCacheData class_cache = getClassCacheData(context, cl, clid);
        return (class_cache == null ? null : class_cache.get());
    }

    static public Set<String> getClassNotFoundCache(CallContext context, ClassLoader cl, String clid) {
        ClassCacheData class_cache = getClassCacheData(context, cl, clid);
        return (class_cache == null ? null : class_cache.getNotFound());
    }

    static public synchronized ClassCacheData getClassCacheData(CallContext context, ClassLoader cl, String clid) {
        if (class_cache_checked == null) {
            class_cache_checked = new HashSet<String>();
        }
        if (class_cache_checked.contains(clid) == false) {
            class_cache_checked.add(clid);
            Class ccc = null;
            try {
                // log(context, "LOOKING FOR CLASS CACHE IN " + clid);
                ccc = Class.forName("com.sphenon.basics.metadata.ClassCacheDataImpl_" + clid.replace(".","_"), true, cl);
            } catch (ClassNotFoundException cnfe) {
                return null;
            }
            Constructor constructor;
            try {
                constructor = ccc.getConstructor();
            } catch (NoSuchMethodException nsme) {
                return null;
            }
            try {
                class_cache = (ClassCacheData) constructor.newInstance();
            } catch (InstantiationException ia) {
                return null;
            } catch (IllegalAccessException iae) {
                return null;
            } catch (InvocationTargetException ite) {
                return null;
            }
            // log(context, "GOT CLASS CACHE IN " + clid);
        }
        return class_cache;
    }

    static protected Map<String,Class>             default_class_cache;
    static protected Set<String>                   default_class_not_found_cache;
    static protected ClassLoader                   default_class_cache_class_loader;
    static protected Throwable default_class_cache_class_loader_throwable;

    static public    Map<String,Map<String,Class>> all_class_caches;
    static public    Map<String,Set<String>>       all_class_not_found_caches;
    static public    boolean                       remember_classes;

    static protected Map<String,Integer>           access_counters;

    static public void dumpAccessCounters(CallContext context) {
        for (String key : access_counters.keySet()) {
            System.err.printf("%09d %s\n", access_counters.get(key), key);
        }
    }

    static public boolean debug_counters = false;
    static public boolean debug_traces = false;
    static protected long not_found_counter = 0;
    static protected long total_not_found_counter = 0;
    static protected long different_not_found_counter = 0;
    static protected String last_not_found_name = null;

    static public Class mustGetClassForName(CallContext context, String name) {
        try {
            return getClassForName(context, name);
        } catch (ClassNotFoundException cnfe) {
            throw new Error("unexpected - class does not exist", cnfe);
        }
    }

    static public Class getClassForName(CallContext context, String name) throws ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        /* if (name.matches(".*cocp.*")) { log(context, "##### - looking for " + name); } */

        if (cache_disabled) { /* if (name.matches(".*cocp.*")) { log(context, "##### - cache disabled"); } */ return Class.forName(name, true, cl); }

        Map<String,Class> cc = null;
        Set<String> cnfc = null;

        if (debug_counters) {
            if (access_counters == null) {
                access_counters = new HashMap<String,Integer>();
            }
            Integer current = access_counters.get(name);
            access_counters.put(name, current == null ? 1 : (current +1));
        }
        if (debug_traces) {
            String st = "";
            StackTraceElement[] stacktrace = (new Throwable()).getStackTrace();
            for (int i=0; i<stacktrace.length && i < 8; i++) {
                StackTraceElement ste = stacktrace[i];
                st += " [" + ste + "]";
            }
            log(context, "Class: " + name + " - " + st);
        }

        ClassLoader parent_cl = null;
        String clid = "default";
        boolean is_new = false;
        if (cl instanceof URLClassLoaderWithId) {
            URLClassLoaderWithId uclwi = (URLClassLoaderWithId) cl;
            clid = uclwi.getId(context);
            cc = uclwi.getClassCache(context);
            cnfc = uclwi.getClassNotFoundCache(context);
            if (cc == null) {
                cc = getClassCache(context, cl, clid);
                if (cc == null) {
                    cc = new java.util.HashMap<String,Class>(50);
                }
                cnfc = getClassNotFoundCache(context, cl, clid);
                if (cnfc == null) {
                    cnfc = new java.util.HashSet<String>(50);
                }
                uclwi.setClassCache(context, cc);
                uclwi.setClassNotFoundCache(context, cnfc);
                is_new = true;
            }
            parent_cl = cl.getParent();
            if (parent_cl != default_class_cache_class_loader) {
//                 log(context, "-------------------------------------------------------------------------------");
                System.err.println("*WARNING* - class cache accessed with URLClassLoaderWithId whose parent is not the default class loader (" + parent_cl + " - " + default_class_cache_class_loader + " - " + clid + ")");
//                 log(context, "...............................................................................");
//                 log(context, "stored one:");
//                 default_class_cache_class_loader_throwable.printStackTrace();
//                 log(context, "...............................................................................");
//                 log(context, "current one:");
//                 (new Throwable()).printStackTrace();
//                 log(context, "-------------------------------------------------------------------------------");

                // see below
                // maybe default_class_cache_class_loader is just null, which means that
                // no access to any class for the default happened before in this run,
                // but which is unlikely (?)
            }
        } else {
            cc = default_class_cache;
            cnfc = default_class_not_found_cache;
            if (cc == null) {
                cc = getClassCache(context, cl, clid);
                if (cc == null) {
                    cc = new java.util.HashMap<String,Class>();
                }
                cnfc = getClassNotFoundCache(context, cl, clid);
                if (cnfc == null) {
                    cnfc = new java.util.HashSet<String>();
                }
                default_class_cache = cc;
                default_class_not_found_cache = cnfc;
                default_class_cache_class_loader = cl;
                default_class_cache_class_loader_throwable = new Throwable();
                is_new = true;
            } else if (default_class_cache_class_loader != cl) {
//                 log(context, "-------------------------------------------------------------------------------");
                System.err.println("*WARNING* - class cache accessed with two different default class loaders (" + cl + " - " + default_class_cache_class_loader + ")");
//                 log(context, "...............................................................................");
//                 log(context, "stored one:");
//                 default_class_cache_class_loader_throwable.printStackTrace();
//                 log(context, "...............................................................................");
//                 log(context, "current one:");
//                 (new Throwable()).printStackTrace();
//                 log(context, "-------------------------------------------------------------------------------");

                // i.e. the following assertion is NOT true:
                // - there is one default class loader in use in the system
                // - there are possibly several project specific class loaders in use,
                //   but all are instances of URLClassLoaderWithId
                // if this happens to be not true, the various other class loaders
                // need to be identifyable and uniquely distingusihable to
                // maintain different caches
            }
        }
        if (is_new) {
            if (all_class_caches == null) {
                all_class_caches = new HashMap<String,Map<String,Class>>(6);
            }
            all_class_caches.put(clid, cc);
            if (all_class_not_found_caches == null) {
                all_class_not_found_caches = new HashMap<String,Set<String>>(6);
            }
            all_class_not_found_caches.put(clid, cnfc);
        }

        /* if (name.matches(".*cocp.*")) { log(context, "##### - loader id: " + clid); } */
        Class c = cc.get(name);
        if (c != null) { /* if (name.matches(".*cocp.*")) { log(context, "##### - in cache"); } */ return c; }
        if (cnfc.contains(name)) { throw new ClassNotFoundException(); }

        if (parent_cl != null && default_class_cache != null) {
            c = default_class_cache.get(name);
            if (c != null) { /* if (name.matches(".*cocp.*")) { log(context, "##### - in default cache"); } */ return c; }
            // das hier wohl eher nicht, denn der parent kann ja gerade typischerweise weniger haben
            // if (default_class_not_found_cache.contains(name)) { throw new ClassNotFoundException(); }
        }

        try {
            c = Class.forName(name, true, cl);
        } catch (ClassNotFoundException cnfe) {
            total_not_found_counter++;
            if (total_not_found_counter % not_found_notice_every == 0) {
                log(context, "" + total_not_found_counter + " classes not found");
            }
            String not_found_name = name.replaceFirst(".*\\.","").replaceFirst("(Class_|Factory_|Retriever_)*", "");

            // stop for debugger...
            // if (not_found_name.equals("UMLModelElement")) {
            //     log(context, "wow");
            // }
            if (last_not_found_name != null && last_not_found_name.equals(not_found_name)) {
                not_found_counter++;
            } else {
                different_not_found_counter++;
                if (not_found_notice_maximum < 0 || different_not_found_counter <= not_found_notice_maximum) {
                    log(context, "Class (or similar) '" + not_found_name + "' not found " + not_found_counter + " times (class loader '" + clid + "')");
                }
                not_found_counter = 0;
                last_not_found_name = not_found_name;
                if (different_not_found_counter <= not_found_dump_maximum) {
                    log(context, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    log(context, com.sphenon.basics.debug.RuntimeStep.getStackDump(context));
                    log(context, "'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''");
                    String st = "";
                    StackTraceElement[] stacktrace = cnfe.getStackTrace();
                    for (int i=0; i<stacktrace.length && i < 16; i++) {
                        StackTraceElement ste = stacktrace[i];
                        st += " [" + ste + "]";
                    }
                    log(context, "Stack: " + name + " - " + st);
                    log(context, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
            }

            if (remember_classes) {
                cnfc.add(name);
                // log(context, "not found, and not yet in cache (" + clid + "): " + name);
                /* if (name.matches(".*cocp.*")) { log(context, "##### - not found, cached"); } */
            }
            throw cnfe;
        }
        // possibly we could extend URLClassLoaderWithId so that it automatically
        // only handles classes for which it is responsible, e.g. by providing a
        // regexp and checking each class - if it does not match, it delegates
        // to it's parent loader in a way so that the class is added to and handled
        // by that loader (e.g. by directly calling "class for name" or so)
        //
        // here, we should check then the class loader attached to the loaded class
        // and put it into the corresponding cache here
        //
        // ...but this developes in the direction of OSGi - hmmmmm....

        if (remember_classes && c != null) {
            cc.put(name, c);
            // log(context, "found, but not yet in cache (" + clid + "): " + name);
            /* if (name.matches(".*cocp.*")) { log(context, "##### - found, cached"); } */
        }

        /* if (name.matches(".*cocp.*")) { log(context, "##### - ok"); } */
        return c;
    }

    static protected class Signature {
        public Signature(Class c, String name, Class... arguments) {
            this.c = c;
            this.name = name;
            this.arguments = arguments;

            this.hc = c.hashCode() ^ name.hashCode();
            if (arguments != null) {
                for (Class argument : arguments) {
                    hc ^= argument.hashCode();
                }
            }
        }
        public Class   c;
        public String  name;
        public Class[] arguments;
        public int     hc;
        public int hashCode() {
            return this.hc;
        }
        public boolean equals(Object o) {
            Signature os = (Signature) o;
            if ( ! (this.c == null ? os.c == null : this.c.equals(os.c))) { return false; }
            if ( ! (this.name == null ? os.name == null : this.name.equals(os.name))) { return false; }
            if (this.arguments == null) {
                return (os.arguments == null);
            } else {
                int l = this.arguments.length;
                if ( ! (l == os.arguments.length)) { return false; }
                for (int i=0; i<l; i++) {
                    Class a1 = this.arguments[i];
                    Class a2 = os.arguments[i];
                    if ( ! (a1 == null ? a2 == null : a1.equals(a2))) { return false; }
                }
                return true;
            }
        }
    }

    static protected Map<Signature, Method> cache;

    static public Method getMethod(Class c, String name, Class... arguments) throws NoSuchMethodException {
        if (cache == null) {
            cache = new HashMap<Signature, Method>();
        }
        Signature signature = new Signature(c, name, arguments);
        Method method = cache.get(signature);
        if (method == null) {
            method = c.getMethod(name, arguments);
            cache.put(signature, method);
        }
        return method;
    }
}
