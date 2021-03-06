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

import java.io.PrintStream;
import com.sphenon.basics.context.*;

public class DumpNode_Stream implements DumpNode {

    protected DumpNode_Stream parent;
    protected Object          currently_dumped;
    protected PrintStream     out;
    protected boolean         first;
    protected boolean         show_names;
    protected boolean         name_value_same_line;
    protected String          indent;
    protected String          indent_increment;
    protected boolean         technical_details;

    public DumpNode_Stream (CallContext context, PrintStream out) {
        this(context, out, "  ", null);
    }
    
    public DumpNode_Stream (CallContext context, PrintStream out, String indent) {
        this(context, out, indent, null);
    }

    public DumpNode_Stream (CallContext context, PrintStream out, String indent, DumpNode_Stream parent) {
        this(context, out, indent, parent, true, true, "   ", true);
    }

    public DumpNode_Stream (CallContext context, PrintStream out, String indent, DumpNode_Stream parent, boolean show_names, boolean name_value_same_line, String indent_increment, boolean technical_details) {
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
        if (first == false || this.name_value_same_line == false) { this.out.print(this.indent); } else { this.first = false; }
        this.out.println(value);
    }


    public void dump(CallContext context, String name, Object value) {
        if (this.first == false || this.name_value_same_line == false) { this.out.print(this.indent); } else { this.first = false; }
        String new_indent = this.indent;
        if (name != null) {
            if (this.show_names) {
                if (this.name_value_same_line) {
                    this.out.print(name + " = ");
                    new_indent += name.replaceAll("."," ") + indent_increment;
                } else {
                    this.out.println(name + ":");
                    new_indent += indent_increment;
                }
            } else {
                new_indent += indent_increment;
            }
        }
        if (value instanceof Dumpable) {
            if (this.isCurrentlyDumped(context, value)) {
                this.out.println("-- recursion: dump discontinued --");
            } else {
                this.currently_dumped = value;
                DumpNode subdn = new DumpNode_Stream(context, this.out, new_indent, this, this.show_names, this.name_value_same_line, this.indent_increment, this.technical_details);
                ((Dumpable)value).dump(context, subdn);
                subdn.close(context);
                this.currently_dumped = null;
            }
        } else if (Dumper.isCommonType(context, value)) {
            DumpNode subdn = new DumpNode_Stream(context, this.out, new_indent, this, this.show_names, this.name_value_same_line, this.indent_increment, this.technical_details);
            Dumper.dumpCommonType(context, value, subdn);
            subdn.close(context);
        } else {
            this.out.println(value == null ? "(null)" : value.toString());
        }
    }

    public DumpNode openDumpTechnicalDetails(CallContext context, String name) {
        return openDump(context, name, true);
    }

    public DumpNode openDump(CallContext context, String name) {
        return openDump(context, name, false);
    }

    public DumpNode openDump(CallContext context, String name, boolean is_technical_detail) {
        if (is_technical_detail && this.technical_details == false) {
            return null;
        }
        String new_indent = this.indent;
        if (this.show_names) {
            if (this.first == false || this.name_value_same_line == false) { this.out.print(this.indent); } else { this.first = false; }
            if (this.name_value_same_line) {
                this.out.print(name + " = ");
                new_indent += name.replaceAll("."," ") + indent_increment;
            } else {
                this.out.println(name + ":");
                new_indent += indent_increment;
            }
        } else {
            new_indent += indent_increment;
        }
        return new DumpNode_Stream(context, this.out, new_indent, this, this.show_names, this.name_value_same_line, this.indent_increment, this.technical_details);
    }

    public void close(CallContext context) {
        if (this.first && this.name_value_same_line) {
            this.out.println("");
            this.first = false;
        }
    }

    public boolean isCurrentlyDumped(CallContext context, Object value) {
        return    (this.currently_dumped != null && this.currently_dumped == value)
               || (this.parent != null && this.parent.isCurrentlyDumped(context, value));
    }
}
