package com.sphenon.basics.context.test;

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
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.notification.BootstrapNotifier;
import com.sphenon.basics.testing.TestResult;
import com.sphenon.basics.testing.TestResult_ExceptionRaised;

import com.sphenon.basics.testing.*;

public class Test_ContextBasic extends com.sphenon.basics.testing.classes.TestBase {

    protected boolean trace = false;

    public Test_ContextBasic (CallContext context) {
        this.trace = true;
    }

    public Test_ContextBasic (CallContext context, boolean trace) {
        this.trace = trace;
    }

    public String getId(CallContext context) {
        if (this.id == null) {
            this.id = "ContextBasic";
        }
        return this.id;
    }

    static public void main(String[] args) {
        // info();

        Context context = com.sphenon.basics.context.classes.RootContext.getRootContext ();
        TestRun test_run = new com.sphenon.basics.testing.classes.ClassTestRun(context);
        TestResult result = (new Test_ContextBasic(context, true)).perform(context, test_run);
        System.err.println(result);
        if (result  == TestResult.OK) {
            System.err.println("TEST OK.");
        } else {
            System.err.println("*** TEST FAILED ***");
        }
    }

    public void f3 (Context context) throws Throwable {
        String result = null;

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f3..." ); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f3, initialising contexts..." ); }

        TestContext test_context = TestContext.get(context);

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f3, test_context: " + result); }
        if ( ! "4711".equals(result)) { throw new Throwable("expected '4711', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f3 done." ); }
    }

    public void f2 (Context context) throws Throwable {
        String result = null;

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2..." ); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2, initialising contexts..." ); }

        Context my_context = Context.create(context);
        TestContext test_context = TestContext.create(my_context);

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2, test_context: " + result); }
        if ( ! "default".equals(result)) { throw new Throwable("expected 'default', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2, setting test_context..."); }

        test_context.setTestValue("4711");

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2, test_context: " + result); }
        if ( ! "4711".equals(result)) { throw new Throwable("expected '4711', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "calling f3..." ); }

        f3(my_context);

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2, test_context: " + result); }
        if ( ! "4711".equals(result)) { throw new Throwable("expected '4711', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f2 done." ); }
    }

    public void f1 (Context context) throws Throwable {
        String result = null;

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f1..." ); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f1, initialising contexts..." ); }

        TestContext test_context = TestContext.get(context);

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f1, test_context: " + result); }
        if ( ! "default".equals(result)) { throw new Throwable("expected 'default', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "calling f2..." ); }

        f2(context);

        result = test_context.getTestValue();
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f1, test_context: " + result); }
        if ( ! "default".equals(result)) { throw new Throwable("expected 'default', got " + result); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "f1 done." ); }
    }

    public TestResult perform (CallContext call_context, TestRun test_run) {
        Context context = Context.create(call_context);

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "main..." ); }

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "main, creating root context..." ); }

        TestContext test_context = TestContext.create(context);

        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "calling f1..." ); }

        try {
            f1(context);
        } catch (Throwable t) {
            return new TestResult_ExceptionRaised(context, t);
        }
        
        if (trace) { BootstrapNotifier.sendCheckpoint(context,  "main done." ); }

        return TestResult.OK;
    }

    static void info () {
        System.err.println("java.vm.specification.version: " + java.lang.System.getProperty("java.vm.specification.version"));
        System.err.println("java.vm.specification.vendor : " + java.lang.System.getProperty("java.vm.specification.vendor"));
        System.err.println("java.vm.specification.name   : " + java.lang.System.getProperty("java.vm.specification.name"));
        System.err.println("java.vm.version              : " + java.lang.System.getProperty("java.vm.version"));
        System.err.println("java.vm.vendor               : " + java.lang.System.getProperty("java.vm.vendor"));
        System.err.println("java.vm.name                 : " + java.lang.System.getProperty("java.vm.name"));
        System.err.println("java.version                 : " + java.lang.System.getProperty("java.version"));
        System.err.println("java.vendor                  : " + java.lang.System.getProperty("java.vendor"));
        System.err.println("java.specification.version   : " + java.lang.System.getProperty("java.specification.version"));
        System.err.println("java.specification.name      : " + java.lang.System.getProperty("java.specification.name"));
        System.err.println("java.specification.vendor    : " + java.lang.System.getProperty("java.specification.vendor"));

        java.lang.Package[] packages = java.lang.Package.getPackages();
        for (int i=0; i<packages.length; i++) {
            java.lang.Package p = packages[i];
            System.err.println(p.getName() + ": " + p.getImplementationTitle() + ", " + p.getImplementationVersion() + ", " + p.getImplementationVendor());
        }
    }
}

/*
java.vm.specification.version: 1.0
java.vm.specification.vendor : Sun Microsystems Inc.
java.vm.specification.name   : Java Virtual Machine Specification
java.vm.version              : 1.4.2_05-b04
java.vm.vendor               : Sun Microsystems Inc.
java.vm.name                 : Java HotSpot(TM) Client VM
java.version                 : 1.4.2_05
java.vendor                  : Sun Microsystems Inc.
java.specification.version   : 1.4
java.specification.name      : Java Platform API Specification
java.specification.vendor    : Sun Microsystems Inc.
java.lang: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.net.www.protocol.file: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.net: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.security: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.security.util: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.net.www: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.nio.charset: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.lang.reflect: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.misc: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.nio.charset.spi: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.util: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.io: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.lang.ref: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.reflect: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.security.cert: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.io: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.net.www.protocol.jar: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.util.zip: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
sun.nio.cs: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
java.nio: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
com.sphenon.basics.context.test: null, null, null
java.util.jar: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
com.sphenon.basics.context: null, null, null
sun.security.action: Java Runtime Environment, 1.4.2_05, Sun Microsystems, Inc.
*/
