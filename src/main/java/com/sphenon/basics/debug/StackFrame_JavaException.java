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

import java.util.List;
import java.util.LinkedList;
import java.util.Vector;

public class StackFrame_JavaException extends StackFrame implements Dumpable {

    protected Throwable exception;

    public StackFrame_JavaException(CallContext context, Throwable exception) {
        super(context);
        this.exception = exception;
//         System.err.println("SFJE");
//         exception.printStackTrace();
    }

    protected List<Stack> sub_stacks;

    public List<Stack> getSubStacks(CallContext context) {
        if (this.sub_stacks == null) {
            this.sub_stacks = new LinkedList<Stack>();
            this.getParentStack(context); // to init myself
            Throwable[] causes;
            if (this.exception instanceof ExceptionWithMultipleCauses) {
                causes = ((ExceptionWithMultipleCauses) this.exception).getCauses(context);
            } else {
                causes = new Throwable[1];
                causes[0] = this.exception.getCause();
            }
            if (causes != null) {
                for (Throwable cause : causes) {
                    if (cause != null) {
                        StackFrame_JavaException sfje = new StackFrame_JavaException(context, cause);
                        Vector<StackFrame> ps = sfje.getParentStack(context, 
                                                                    jstack == null ? null : jstack[throw_pos + 1],
                                                                    jstack == null ? -1 : jstack.length - 1 - throw_pos - 1,
                                                                    rsstack == null || rsstack.size() == 0 ? null : ((LinkedList<RuntimeStep>) (rsstack.get(rsstack.size() - 1))).getLast(),
                                                                    rsstack == null || rsstack.size() == 0 ? -1 : rsstack.size() - 1);
                        this.sub_stacks.add(Stack.create(context, ps));
                    }
                }
            }
        }

        return this.sub_stacks;
    }
    
    public Vector<StackFrame> getParentStack(CallContext context) {
        return getParentStack(context, null, -1, null, -1);
    }

    protected Vector<StackFrame> parent_stack;
    protected List<List<RuntimeStep>> rsstack;
    protected StackTraceElement[] jstack;
    protected int throw_pos = 0;

    protected Vector<StackFrame> getParentStack(CallContext context, StackTraceElement root_jste, int root_jdepth, RuntimeStep root_rsste, int root_rsdepth) {
        if (this.parent_stack == null) {
            this.parent_stack = new Vector<StackFrame>();
        
            jstack = this.exception.getStackTrace();
            int jsl = jstack.length;
            if (root_jste != null && jstack[jsl-1-root_jdepth].equals(root_jste) == false) {
                System.err.println("***ASSERTION FAILED*** root java stack trace element not found at specified position");
                System.err.println("                       " + root_jste.toString());
                System.err.println("                       " + jstack[jsl-1-root_jdepth].toString());
            }

            throw_pos = 0;
            while (jsl > throw_pos) {
                String jstext = jstack[throw_pos].toString();
                if (jstext.matches("(?:(?:com\\.sphenon\\.basics\\.(?:(?:exception\\.Exception.*\\.createAndThrow)|(?:customary\\.CustomaryContext\\.throw.*)))|(?:[a-z0-9_\\.]+\\.returncodes\\.([A-Za-z0-9_\\.]+)\\.create(?:(?:AndThrow)|(?:\\1))))\\(.*")) {
                    throw_pos++;
                } else {
                    break;
                }
            }

            rsstack = null;
            int next_rsjdepth = -1;
            int next_rsdepth = -1;
            StackTraceElement next_rsj = null;
            RuntimeStep next_rs = null;
            if (this.exception instanceof WithContext) {
                rsstack = RuntimeStep.getRuntimeStepStack(((WithContext) this.exception).getContext());

                if (root_rsste != null && (rsstack.size() <= root_rsdepth || ((LinkedList<RuntimeStep>)(rsstack.get(root_rsdepth))).getLast().equals(root_rsste) == false)) {
                    System.err.println("***ASSERTION FAILED*** root runtime step stack trace element not found at specified position (size: " + rsstack.size() + ", depth: " + root_rsdepth + ")");
                }
                
                next_rsdepth = root_rsdepth + 1;
                while (next_rsdepth < rsstack.size()) {
                    next_rs = ((LinkedList<RuntimeStep>)(rsstack.get(next_rsdepth))).getLast();

                    StackTraceElement[] rs_jstack = next_rs.getCreationStackTrace(context);
                    if (rs_jstack == null && next_rs.getException(context) != null) {
                        rs_jstack = next_rs.getException(context).getStackTrace();
                    }
                    if (rs_jstack == null) {
                        System.err.println("***ASSERTION FAILED*** no creation stack trace available for runtime step");
                        System.err.println(ContextAware.ToString.convert(context, next_rs));
                    } else {
                        int rs_jsl = rs_jstack.length;
                        int rs_pos = 0;
                        while (rs_jsl > rs_pos) {
                            String rs_jstext = rs_jstack[rs_pos].toString();
                            if (rs_jstext.matches("com\\.sphenon\\.basics\\.debug\\.RuntimeStep(?:(?:Impl)|(?:FactoryImpl))?\\.[<>A-Za-z0-9_]+\\(.*")) {
                                rs_pos++;
                            } else {
                                break;
                            }
                        }
                        next_rsjdepth = rs_jstack.length - 1 - rs_pos - 1;
                        next_rsj = rs_jstack[rs_pos+1];
                        break;
                    }
                    next_rsdepth++;
                }
            }

            for (int depth = root_jdepth + 1; depth < jsl-throw_pos; depth++) {
                if (next_rsj != null && next_rsjdepth == depth) {
                    if (jstack[jsl-1-depth].equals(next_rsj) == false) {
                        System.err.println("***ASSERTION FAILED*** runtime step parent stack trace element not found at assumed position");
                        System.err.println("                       " + next_rsj.toString());
                        System.err.println("                       " + jstack[jsl-1-depth].toString());
                    }
                }            
                if (next_rsj != null && next_rsjdepth < depth) {
                    this.parent_stack.add(new StackFrame_RuntimeStep(context, next_rs));
                    
                    next_rsj = null;
                    next_rsdepth++;
                    while (next_rsdepth < rsstack.size()) {
                        next_rs = ((LinkedList<RuntimeStep>)(rsstack.get(next_rsdepth))).getLast();

                        StackTraceElement[] rs_jstack = next_rs.getCreationStackTrace(context);
                        if (rs_jstack == null && next_rs.getException(context) != null) {
                            rs_jstack = next_rs.getException(context).getStackTrace();
                        }
                        if (rs_jstack == null) {
                            System.err.println("***ASSERTION FAILED*** no creation stack trace available for runtime step");
                            System.err.println(ContextAware.ToString.convert(context, next_rs));
                        } else {
                            int rs_jsl = rs_jstack.length;
                            int rs_pos = 0;
                            while (rs_jsl > rs_pos) {
                                String rs_jstext = rs_jstack[rs_pos].toString();
                                if (rs_jstext.matches("com\\.sphenon\\.basics\\.debug\\.RuntimeStep(?:(?:Impl)|(?:FactoryImpl))?\\.[<>A-Za-z0-9_]+\\(.*")) {
                                    rs_pos++;
                                } else {
                                    break;
                                }
                            }
                            next_rsjdepth = rs_jstack.length - 1 - rs_pos - 1;
                            next_rsj = rs_jstack[rs_pos+1];
                            break;
                        }
                        next_rsdepth++;
                    }
                }
                
                this.parent_stack.add(new StackFrame_JavaInstruction(context, jstack[jsl-1-depth]));
            }
            
            this.parent_stack.add(this);
        }

        return this.parent_stack;
    }
    
    public void dump(CallContext context, DumpNode dump_node) {
        String message;
        if (exception instanceof ExceptionWithHelpMessage) {
            message = ((ExceptionWithHelpMessage) exception).getMessage(false);
            String hm = ((ExceptionWithHelpMessage) exception).getHelpMessage();
            if (hm != null) {
                message += " - " + hm;
            }
        } else {
            message = ContextAware.ToString.convert(context, exception);
        }
        dump_node.dump(context, "Exception", message);
        List<Stack> sub_stacks = this.getSubStacks(context);
        if (sub_stacks != null && sub_stacks.size() != 0) {
            DumpNode dn = dump_node.openDump(context, "Causes ");
            int i=0;
            for (Stack sub_stack : sub_stacks) {
                dn.dump(context, (new Integer(i++)).toString(), sub_stack);
            }
            dn.close(context);
        }
    }
}
