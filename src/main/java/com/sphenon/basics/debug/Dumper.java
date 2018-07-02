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

import java.lang.reflect.*;
import java.lang.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Array;

public class Dumper {

    static protected Factory_DumpNode factory;

    static public void setFactory(CallContext context, Factory_DumpNode new_factory) {
        factory = new_factory;
    }

    /**
       Dumps the contents of the given instance in a human readable form, intended for
       debugging purposes. If the instance if derived from {@link Dumpable},
       that interface, otherwise, the simple toString method is used to dump
       the content. The procedure is applied recursively.
       
       @param name      the name of the instance, or some kind of identifier of this dump
       @param instance  the instance to be dumped
    */
    static public void dump(CallContext context, String name, Object instance) {
        if (factory == null) {
            factory = new Factory_DumpNode(context);
        }
        DumpNode dump_node = factory.create(context);
        dump_node.dump(context, name, instance);
    }

    static public void dump(CallContext context, String name, Object instance, String indent, boolean show_names, boolean name_value_same_line, String indent_increment, boolean technical_details) {
        if (factory == null) {
            factory = new Factory_DumpNode(context);
        }
        DumpNode dump_node = factory.create(context, indent, show_names, name_value_same_line, indent_increment, technical_details);
        dump_node.dump(context, name, instance);
    }

    /**
       Dumps the contents of the given instance to a string.
       
       @param name      the name of the instance, or some kind of identifier of this dump
       @param instance  the instance to be dumped
       @return          the dump
    */
    static public String dumpToString(CallContext context, String name, Object instance) {
        return DumpNode_StringBuilder.dumpToString(context, name, instance);
    }

    /**
       Used to check whether this object is a common type and can be dumped
       with the dumpCommonType method, which acts like a dump-Method on an
       instance which implements the {@link Dumpable} interface.

       @param instance The instance to check
       @return true, if the instance can be dumped with dumpCommonType
     */
    static public boolean isCommonType(CallContext context, Object instance) {
        return (    instance != null
                 && (    (instance instanceof Throwable)
                      || (instance.getClass().isArray())
                      || (instance instanceof Map)
                    )
               );
    }

    /**
       Utility method to dump common java library types which cannot be
       derived from Dumpable.

       @param instance The instance to dump
       @param dump_node The dump node to dump to
     */
    static public void dumpCommonType(CallContext context, Object instance, DumpNode dump_node) {
        if (instance == null) {
            dump_node.dump(context, "(null)");
        } else if (instance instanceof Throwable) {
            if (true) {
                Stack stack = Stack.create(context, ((Throwable) instance));
                dump_node.dump(context, "Exception ", stack);
            } else {
                dump_node.dump(context, "Name      ", instance.toString());
                DumpNode stack_dump_node = dump_node.openDumpTechnicalDetails(context, "StackTrace");
                if (stack_dump_node != null) {
                    for (StackTraceElement SE : ((Throwable) instance).getStackTrace()) {
                        stack_dump_node.dump(context, SE.toString());
                    }
                    stack_dump_node.close(context);
                }
                Throwable cause = ((Throwable) instance).getCause();
                if (cause != null) {
                    dump_node.dump(context, "Cause     ", cause);
                }
            }
        } else if (instance.getClass().isArray()) {
            DumpNode array_dump_node = dump_node.openDump(context, instance.getClass().getComponentType().getName().replaceFirst(".*\\.","") + "[]");
            for (int i=0; i<Array.getLength(instance); i++) {
                array_dump_node.dump(context, (new Integer(i)).toString(), Array.get(instance, i));
            }
            array_dump_node.close(context);
        } else if (instance instanceof Map) {
            DumpNode array_dump_node = dump_node.openDump(context, instance.getClass().getName().replaceFirst(".*\\.",""));
            Map map = (Map) instance;
            for (Object key : map.keySet()) {
                array_dump_node.dump(context, ContextAware.ToString.convert(context, key), map.get(key));
            }
            array_dump_node.close(context);
        } else {
            dump_node.dump(context, instance.toString());
        }
    }

    /**
       Utility method to dump classes and other reflection types

       @param instance The instance to dump
       @param dump_node The dump node to dump to
     */
    static public void dumpClass(CallContext context, Class c, DumpNode dump_node, HashMap<Class,Class> already_dumped) {
        if (dump_node == null) {
            if (factory == null) {
                factory = new Factory_DumpNode(context);
            }
            dump_node = factory.create(context);
        }
        if (already_dumped == null) {
            already_dumped = new HashMap<Class,Class>();
        }

        if (c.isArray()) {
            dump_node.dump(context, "Array       ", c.getComponentType().getName().replaceFirst(".*\\.","") + " []");
        } else {
            String tpstring = "";
            TypeVariable[] tvs = c.getTypeParameters();
            if (tvs != null && tvs.length != 0) {
                for (TypeVariable tv : tvs) {
                    tpstring += (tpstring.length() == 0 ? " <" : ", ") + tv.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2");
                }
                tpstring += ">";
            }
            dump_node.dump(context, c.isEnum() ? "Enum        " : c.isInterface() ? "Interface   " : "Class       ", c.getName().replaceFirst(".*\\.","") + tpstring + (already_dumped.get(c) != null ? " * ALREADY DUMPED *" : ""));
            dump_node.dump(context, "Package     ", c.getPackage().getName());
            dump_node.dump(context, "Properties  ", 
                              Modifier.toString(c.getModifiers())
                           + (c.isAnnotation()     ? " annotation" : "")
                           + (c.isAnonymousClass() ? " anonymous" : "")
                           + (c.isLocalClass()     ? " local" : "")
                           + (c.isMemberClass()    ? " member" : "")
                           + (c.isSynthetic()      ? " synthetic" : "")
                          );

            // ProtectionDomain getProtectionDomain()

            if (already_dumped.get(c) == null) {
                already_dumped.put(c, c);

                {
                    Class       dc  = c.getDeclaringClass();
                    if (dc != null) {
                        dump_node.dump(context, "DeclaredIn  ", dc.getName().replaceFirst(".*\\.",""));
                    }
                }
                {
                    Class       ec  = c.getEnclosingClass();
                    if (ec != null) {
                        dump_node.dump(context, "EnclosedIn  ", ec.getName().replaceFirst(".*\\.",""));
                    }
                }
                {
                    Constructor eco = c.getEnclosingConstructor();
                    if (eco != null) {
                        dump_node.dump(context, "EnclosedIn  ", eco.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                    }
                }
                {
                    Method      em  = c.getEnclosingMethod();
                    if (em != null) {
                        dump_node.dump(context, "EnclosedIn  ", em.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                    }
                }

                {
                    Class sc = c.getSuperclass();
                    // getGenericSuperclass()
                    if (sc != null && sc != Object.class) {
                        DumpNode dn2 = dump_node.openDump(context, "Superclass  ");
                        dumpClass(context, sc, dn2, already_dumped);
                    }
                }
                {
                    // Class[] ics = c.getInterfaces();
                    Type[] gis = c.getGenericInterfaces();
                    if (gis != null && gis.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Interfaces  ");
                        for (Type gi : gis) {
                            if (gi instanceof Class) {
                                dumpClass(context, (Class) gi, dn2, already_dumped);
                            } else {
                                dn2.dump(context, "Type", gi);
                            }
                        }
                    }
                }

                {
                    Annotation[] as = c.getDeclaredAnnotations();
                    if (as != null && as.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Annotations ");
                        for (Annotation a : as) {
                            dn2.dump(context, a.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }

                {
                    Class[] dcs = c.getDeclaredClasses();
                    if (dcs != null && dcs.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Classes     ");
                        for (Class dc : dcs) {
                            dn2.dump(context, dc.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }

                {
                    Constructor[] dcos = c.getDeclaredConstructors();
                    if (dcos != null && dcos.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Constructors");
                        for (Constructor dco : dcos) {
                            dn2.dump(context, dco.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }

                {
                    Field[] dcfs = c.getDeclaredFields();
                    if (dcfs != null && dcfs.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Fields      ");
                        for (Field dcf : dcfs) {
                            dn2.dump(context, dcf.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }

                {
                    Method[] dcms = c.getDeclaredMethods();
                    if (dcms != null && dcms.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Methods     ");
                        for (Method dcm : dcms) {
                            dn2.dump(context, dcm.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }

                {
                    Object[] ecs = c.getEnumConstants();
                    if (ecs != null && ecs.length != 0) {
                        DumpNode dn2 = dump_node.openDump(context, "Constants   ");
                        for (Object ec : ecs) {
                            dn2.dump(context, ec.toString().replaceAll("([A-Za-z0-9_.]+\\.)([A-Za-z0-9_]+)", "$2"));
                        }
                    }
                }
            }
        }
    }
}






