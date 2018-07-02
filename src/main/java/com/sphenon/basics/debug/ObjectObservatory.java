package com.sphenon.basics.debug;

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
import java.lang.reflect.Method;

public class ObjectObservatory {

    static public class Fixture {
        public String id;
        public Object object;
        public String method_regexp;
    }

    static public Vector<Fixture> specimens;
    static public Fixture last_specimen;
    static public boolean wait_enabled = false;
    static public boolean observatory_enabled = false;

    static public void addSpecimen(String id, Object object, String method_regexp) {
        addSpecimen(id, object, method_regexp, false);
    }

    static public Fixture getSpecimen(String id) {
        if (specimens != null) {
            for (Fixture fixture : specimens) {
                if (fixture.id.equals(id)) {
                    return fixture;
                }
            }
        }
        return null;
    }

    static public void listSpecimens() {
        if (specimens != null) {
            for (Fixture fixture : specimens) {
                System.err.printf("  - %16s : %s\n", fixture.id, fixture.object);
            }
        }
    }

    synchronized static public void addSpecimen(String id, Object object, String method_regexp, boolean wait) {
        if (observatory_enabled == false) {
            // System.err.println("Observatory not enabled, item '" + id + "' ignored");
            return;
        }

        if (specimens == null) {
            specimens = new Vector<Fixture>();
        } else {
            for (int i=0; i<specimens.size(); i++) {
                if (specimens.get(i).id != null && specimens.get(i).id.equals(id)) {
                    specimens.remove(i);
                    break;
                }
            }
        }
        last_specimen = new Fixture();
        last_specimen.id = id;
        last_specimen.object = object;
        last_specimen.method_regexp = method_regexp;
        specimens.add(last_specimen);

        if (wait && wait_enabled) {
            try {
                ObjectObservatory.class.wait();
            } catch (java.lang.InterruptedException ie) {
            }
        }
    }

    synchronized static public void clear() {
        specimens = null;
    }

    static public void checkInvocation(CallContext context, Object proxy, Object target, Method method, Object[] arguments) {
        if (specimens == null) { return; }
        String result = null;
        for (Fixture specimen : specimens) {
            if (    specimen.object == proxy
                 && specimen.method_regexp != null
                 && method.getName().matches(specimen.method_regexp)
               ) {
                result = "break here";
            }
        }
    }

    synchronized static public void continueAllThreads() {
        ObjectObservatory.class.notifyAll();
    }
}
