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

/**
   Represents a named node in a tree of dumped values. Serves as an interface
   for dump code, to allow dumping to different structured output channels
   (e.g. stream based standard output, notification channels or some tree
   widget).

   To detect recursion, DumpNodes maintain a stack of currently dumped
   values. Therefore DumpNodes are not reentrant.
 */
public interface DumpNode {

    /**
       Dumps the given value to this dump node instance. Used for simple values.
       This method may be invoked multiple times, but not intermangled with the
       other dump method. If it is called repeatedly, the sequence of invocations
       is considered to represent a multiline value.

       @param value   The value to be dumped.
    **/
    public void     dump(CallContext context, String value);

    /**
       Dumps the given named value to this dump node instance. Used for complex values.
       This method may be invoked multiple times, but not intermangled with the
       other dump method.

       @param name    The name of the new value to be dumped.
       @param value   The value to be dumped.
    **/
    public void dump(CallContext context, String name, Object value);

    /**
       Opens a new named node for dumping and returns it. The returned node is
       used to dump another block of structured data which does not implement
       the Dumpable interface and therefore cannot be dumped automatically
       with the other method above.

       @param name    The name of the new value to be dumped.
    **/
    public DumpNode openDump(CallContext context, String name);

    /**
       Opens a new named node for dumping technical details.

       @param name    The name of the new value to be dumped.
    **/
    public DumpNode openDumpTechnicalDetails(CallContext context, String name);

    /**
       An opened dumpnode shall be closed. This allows the node to adjust it's
       output in case nothing was actually dumped (i.e. the node was not used
       otherwise).
    **/
    public void close(CallContext context);
}
