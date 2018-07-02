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
import com.sphenon.basics.context.classes.*;

import java.net.*;
import java.security.ProtectionDomain;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class DebugClassLoader extends URLClassLoader {

    public DebugClassLoader (ClassLoader parent) {
        super(new URL[0], parent);
    }

    static protected Map<String,Integer> access_counters;

    static public void dumpAccessCounters() {
        for (String key : access_counters.keySet()) {
            int sep = key.indexOf(' ');
            String method = key.substring(0, sep);
            String classname = key.substring(sep+1);
            System.err.printf("%s %09d %s\n", method, access_counters.get(key), classname);
        }
    }

    static public void logAccess(String access) {
        if (access_counters == null) {
            access_counters = new HashMap<String,Integer>();
        }
//         if (access.equals("loadClass com.sphenon.basics.encoding.Encoding")) {
//             // debug_traces
//             String st = "";
//             StackTraceElement[] stacktrace = (new Throwable()).getStackTrace();
//             for (int i=0; i<stacktrace.length && i < 16; i++) {
//                 StackTraceElement ste = stacktrace[i];
//                 st += " [" + ste + "]";
//             }
//             System.err.println("ENC: " + st);
//         }
        Integer current = access_counters.get(access);
        access_counters.put(access, current == null ? 1 : (current +1));
    }

    protected  Class<?> findClass(String name) throws ClassNotFoundException {
        logAccess("findClass " + name);
        return super.findClass(name);
    }

    protected  String findLibrary(String libname) {
        logAccess("findLibrary " + libname);
        return super.findLibrary(libname);
    }

    public  URL findResource(String name) {
        logAccess("findResource " + name);
        return super.findResource(name);
    }

    public  Enumeration<URL> findResources(String name) throws IOException {
        logAccess("findResources " + name);
        return super.findResources(name);
    }

    protected  Package getPackage(String name) {
        logAccess("getPackage " + name);
        return super.getPackage(name);
    }

    public URL getResource(String name) {
        logAccess("getResource " + name);
        return super.getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
        logAccess("getResourceAsStream " + name);
        return super.getResourceAsStream(name);
    }

    public Enumeration<URL> getResources(String name) throws IOException {
        logAccess("getResources " + name);
        return super.getResources(name);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        logAccess("loadClass " + name);
        return super.loadClass(name);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        logAccess("loadClass " + name);
        return super.loadClass(name, resolve);
    }
}
