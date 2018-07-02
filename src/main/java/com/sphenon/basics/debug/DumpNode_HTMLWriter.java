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

import java.io.PrintWriter;
import com.sphenon.basics.context.*;


/*

incomplete

    protected boolean             show_names;
    protected boolean             name_value_same_line;
    protected String              indent_increment;
    protected boolean             technical_details;

wird noch nicht ausgewertet

 */


public class DumpNode_HTMLWriter implements DumpNode {

    protected DumpNode_HTMLWriter parent;
    protected Object              currently_dumped;
    protected PrintWriter         out;
    protected boolean             first;
    protected boolean             show_names;
    protected boolean             name_value_same_line;
    protected int                 indent;
    protected String              indent_increment;
    protected boolean             technical_details;

    public DumpNode_HTMLWriter (CallContext context, PrintWriter out) {
        this(context, out, 0, null);
    }
    
    public DumpNode_HTMLWriter (CallContext context, PrintWriter out, int indent) {
        this(context, out, indent, null);
    }

    public DumpNode_HTMLWriter (CallContext context, PrintWriter out, int indent, DumpNode_HTMLWriter parent) {
        this(context, out, indent, parent, true, true, "   ", false);
    }

    public DumpNode_HTMLWriter (CallContext context, PrintWriter out, int indent, DumpNode_HTMLWriter parent, boolean show_names, boolean name_value_same_line, String indent_increment, boolean technical_details) {
        this.out    = out;
        this.first  = true;
        this.name_value_same_line = name_value_same_line;
        this.show_names = show_names;
        this.indent = indent;
        this.indent_increment = indent_increment;
        this.parent = parent;
        this.technical_details = technical_details;
    }
    
    public void dump(CallContext context, String value) {
        this.out.print("<tr>" + (this.indent == 0 ? "" : ("<td colspan=\"" + indent + "\"></td>")) + "<td>");
        this.out.print(value);
        this.out.println("</td></tr>");
    }


    public void dump(CallContext context, String name, Object value) {
        this.out.print("<tr>" + (this.indent == 0 ? "" : ("<td colspan=\"" + indent + "\"></td>")) + "<th>" + name + "</th><td>");
        if (value instanceof Dumpable) {
            if (this.isCurrentlyDumped(context, value)) {
                this.out.println("-- recursion: dump discontinued --");
            } else {
                this.currently_dumped = value;
                DumpNode subdn = new DumpNode_HTMLWriter(context, this.out, this.indent + 1, this, this.show_names, this.name_value_same_line, this.indent_increment, this.technical_details);
                ((Dumpable)value).dump(context, subdn);
                subdn.close(context);
                this.currently_dumped = null;
            }
        } else if (Dumper.isCommonType(context, value)) {
            DumpNode subdn = new DumpNode_HTMLWriter(context, this.out, this.indent + 1, this, this.show_names, this.name_value_same_line, this.indent_increment, this.technical_details);
            Dumper.dumpCommonType(context, value, subdn);
            subdn.close(context);
        } else {
            this.out.println(value == null ? "(null)" : value.toString());
        }
        this.out.println("</td></tr>");
    }

    public DumpNode openDumpTechnicalDetails(CallContext context, String name) {
        return openDump(context, name, true);
    }

    public DumpNode openDump(CallContext context, String name) {
        return openDump(context, name, false);
    }

    public DumpNode openDump(CallContext context, String name, boolean is_technical_detail) {
        this.out.println("<tr>" + (this.indent == 0 ? "" : ("<td colspan=\"" + indent + "\"></td>")) + "<th>" + name + "</th></tr>");
        return new DumpNode_HTMLWriter(context, this.out, this.indent + 1, this);
    }

    public void close(CallContext context) {
    }

    public boolean isCurrentlyDumped(CallContext context, Object value) {
        return    (this.currently_dumped != null && this.currently_dumped == value)
               || (this.parent != null && this.parent.isCurrentlyDumped(context, value));
    }
}
