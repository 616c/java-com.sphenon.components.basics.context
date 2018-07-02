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

import java.lang.reflect.*;

public class DebugStarter {

    static public void main(String[] args) {
        final String class_name = args[0];
        final String[] my_args = new String[args.length -1];
        for (int i=1; i<args.length; i++) {
            my_args[i-1] = args[i];
        }

        final DebugClassLoader cl = new DebugClassLoader(Thread.currentThread().getContextClassLoader());
        Thread t = new Thread() {
                public void run () {
                    try {
                        this.setContextClassLoader(cl);
                        Class main_class = Class.forName(class_name, true, cl);
                        Method main = main_class.getMethod("main", String[].class);
                        main.invoke(null, (Object) my_args);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            };
        t.start();
        try {
            t.join();
        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } finally {
            cl.dumpAccessCounters();
        }
    }
}
