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

/*<Doclet>
    <docl:Entity>ctn://Class<Java>/RootContext</docl:Entity>
    <docl:Definition>
      <section>
        <title>Definition</title>
        <para>
          Provides access to initial contexts.
        </para>
      </section>
    </docl:Definition>
  </Doclet>*/

public class RootContext {
    /*<Doclet>
        <docl:Entity>ctn://Method<Java>/getRootContext</docl:Entity>
        <docl:Interface>
          <para>Intended to be used only in the application
            <quote>root</quote> area, whatever this is in your case.</para>
        </docl:Interface>
      </Doclet>*/
    public static Context getRootContext () {
        return ContextClass.getSingleton();
    }

    static protected ThreadLocal<CallContext> fallback_context;

    /*<Doclet>
        <docl:Entity>ctn://Method<Java>/getFallbackCallContext</docl:Entity>
        <docl:Interface>
          <para>To be used in methods where there is no other
            call context, e.g. 'toString' from Object.</para>
        </docl:Interface>
      </Doclet>*/
    public static CallContext getFallbackCallContext () {
        CallContext cc = null;
        if (fallback_context != null && (cc = fallback_context.get()) != null) {
            return cc;
        }
        return ContextClass.getSingleton();
    }

    /**
       Sets the thread local fallback context

       @return previous value of fallback context
     */
    public static CallContext setFallbackCallContext (CallContext cc) {
      // Debug Code
      //{
      //  StackTraceElement[] trace= Thread.currentThread().getStackTrace();
      //  int i=2;
      //  System.err.println(trace[i].getClassName() + "." + trace[i].getMethodName() + 
      //   "[" + trace[i].getFileName() + ":" + trace[i].getLineNumber() + "] Fallback is now " + cc );
      //} 
        if (fallback_context == null) {
            fallback_context = new ThreadLocal<CallContext>();
        }
        CallContext previous = fallback_context.get();
        fallback_context.set(cc);
        return previous;
    }

    /*<Doclet>
        <docl:Entity>ctn://Method<Java>/getInitialisationContext</docl:Entity>
        <docl:Interface>
          <para>To be used in static initialisers.</para>
        </docl:Interface>
      </Doclet>*/
    public static CallContext getInitialisationContext () {
        return ContextClass.getSingleton();
    }

    /*<Doclet>
        <docl:Entity>ctn://Method<Java>/getDestructionContext</docl:Entity>
        <docl:Interface>
          <para>To be used in finalizers.</para>
        </docl:Interface>
      </Doclet>*/
    public static CallContext getDestructionContext () {
        return ContextClass.getSingleton();
    }

    /*<Doclet>
        <docl:Entity>ctn://Method<Java>/createLocationContext</docl:Entity>
        <docl:Interface>
          <para>To create fresh new Contexts stored locally at specific locations.</para>
        </docl:Interface>
      </Doclet>*/
    public static LocationContext createLocationContext () {
        return ContextClass.createLocationContext();
    }
}
