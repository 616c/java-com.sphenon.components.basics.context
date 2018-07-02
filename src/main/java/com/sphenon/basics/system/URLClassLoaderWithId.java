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
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class URLClassLoaderWithId extends URLClassLoader {

    protected String      id;
    protected boolean     is_active;
    protected String      parent_class_include;
    protected Pattern     parent_class_include_pattern;
    protected String      parent_class_exclude;
    protected Pattern     parent_class_exclude_pattern;
    protected Set<String> parent_class_include_set;
    protected Set<String> parent_class_exclude_set;
    protected ClassLoader filtered_parent;

    public String getId (CallContext context) {
        return this.id;
    }

    // IncludeRegExp
    //
    // ExcludeRegExp
    //
    // see comment in "ClassCache.java"

    static protected java.lang.reflect.Method findLoadedClass;

    public boolean isLoaded(String name) {
        if (super.findLoadedClass(name) != null) { return true; }
        ClassLoader to_check = this;
        if (findLoadedClass == null) {
            try {
                findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
                findLoadedClass.setAccessible(true);
            } catch (NoSuchMethodException nsme) {
                throw new Error("This really is unexpected.", nsme);
            }
        }
        while ((to_check = to_check.getParent()) != null) {
            try {
                if (findLoadedClass.invoke(to_check, name) != null) { return true; }
            } catch (java.lang.reflect.InvocationTargetException ite) {
                throw new Error("Cannot check load state.", ite);
            } catch (IllegalAccessException iae) {
                throw new Error("Cannot check load state.", iae);
            }
        }
        return false;
    }

    static public Vector<URLClassLoaderWithId> all_loaders;
    protected void addMe() {
        if (all_loaders == null) {
            all_loaders = new Vector<URLClassLoaderWithId>();
        }
        all_loaders.add(this);
    }

    public URLClassLoaderWithId (URL[] urls, ClassLoader parent, String id) {
        this(urls, parent, id, null, null);
    }

    public URLClassLoaderWithId (URL[] urls, ClassLoader parent, String id, String parent_class_include) throws PatternSyntaxException {
        this(urls, parent, id, parent_class_include, null);
    }

    public URLClassLoaderWithId (URL[] urls, ClassLoader parent, String id, String parent_class_include, String parent_class_exclude) throws PatternSyntaxException {
        this(urls, parent, id, parent_class_include, parent_class_exclude, (parent_class_include != null || parent_class_exclude != null ? true : false));
    }

    public URLClassLoaderWithId (URL[] urls, ClassLoader parent, String id, String parent_class_include, String parent_class_exclude, boolean filtered) throws PatternSyntaxException {
        super(urls, filtered ? null : parent);
        this.id = id;
        this.parent_class_include = parent_class_include;
        this.parent_class_include_pattern = (parent_class_include == null ? null : Pattern.compile(parent_class_include));
        this.parent_class_exclude = parent_class_exclude;
        this.parent_class_exclude_pattern = (parent_class_exclude == null ? null : Pattern.compile(parent_class_exclude));
        this.filtered_parent = (filtered ? parent : null);
        this.is_active = true;
        this.addMe();
    }

    public void close(CallContext context) {
        this.is_active = false;
    }

    protected void check() {
        if (this.is_active == false) { throw new Error("attempt to use inactive url class loader"); }
    }

    public void clearAssertionStatus() {
        check();
        super.clearAssertionStatus();
    }

    protected  Package definePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) {
        check();
        return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    protected  Class<?> findClass(String name) throws ClassNotFoundException {
        check();
        return super.findClass(name);
    }

    protected  String findLibrary(String libname) {
        check();
        return super.findLibrary(libname);
    }

    public  URL findResource(String name) {
        check();
        return super.findResource(name);
    }

    public  Enumeration<URL> findResources(String name) throws IOException {
        check();
        return super.findResources(name);
    }

    protected  Package getPackage(String name) {
        check();
        return super.getPackage(name);
    }

    protected  Package[] getPackages() {
        check();
        return super.getPackages();
    }

    public URL getResource(String name) {
        check();
        return super.getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
        check();
        return super.getResourceAsStream(name);
    }

    public Enumeration<URL> getResources(String name) throws IOException {
        check();
        return super.getResources(name);
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        check();
        return super.loadClass(name);
    }

    public void addIncludedClass(String name) {
        if (parent_class_include_set == null) {
            parent_class_include_set = new HashSet<String>();
        }
        parent_class_include_set.add(name);
    }

    public void addExcludedClass(String name) {
        if (parent_class_exclude_set == null) {
            parent_class_exclude_set = new HashSet<String>();
        }
        parent_class_exclude_set.add(name);
    }

    /**
       This method is basically a copy of the implementation of URLClassLoader.
       Be aware, that this also changes over time, it happens.

       The difference is, in case there is a filtered parent, this method here
       checks whether to delegate to that filtered_parent based on regexps and
       sets. Also, if not using the parent or not found in the parent, it does
       not fall back to the 'findBootstrapClass0(name)', since this would
       reintroduce all the stuff found in the classpath, which we want to
       avoid.

       That new call to findBootstrapClass0(name) in the superclass caused
       much trouble to debug, since it silently caused newly compiled classes
       not to be reloaded properly.

       Furthermore, note the 'replaceFirst...$...' call below. It is
       important, since it guarantees that also inner classes are also loaded
       via the proper class loader.
     */
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        check();
        if (this.filtered_parent != null) {
            boolean try_filtered_parent = false;
            String name_to_check = name.replaceFirst("\\$.*$",""); // this is indeed important
            if (    (    this.parent_class_include_pattern == null
                      || this.parent_class_include_pattern.matcher(name_to_check).matches() == true
                    )
                 && (    this.parent_class_exclude_pattern == null
                      || this.parent_class_exclude_pattern.matcher(name_to_check).matches() == false
                    )
                 && (    this.parent_class_include_set == null
                      || this.parent_class_include_set.contains(name_to_check) == true
                    )
                 && (    this.parent_class_exclude_set == null
                      || this.parent_class_exclude_set.contains(name_to_check) == false
                    )
               ) {
                try_filtered_parent = true;
            }
            // java 1.7: synchronized (getClassLoadingLock(name)) {
            synchronized (this) {
                Class c = findLoadedClass(name);
                if (c == null) {
                    if (try_filtered_parent) {
                        try {
                            c = filtered_parent.loadClass(name);
                        } catch (ClassNotFoundException e) {
                        }
                    }
                    if (c == null) {
                        c = findClass(name);
                    }
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        } else {
            Class c = super.loadClass(name, resolve);
            return c;
        }
    }

    public void setClassAssertionStatus(String className, boolean enabled) {
        check();
        super.setClassAssertionStatus(className, enabled);
    }

    public void setDefaultAssertionStatus(boolean enabled) {
        check();
        super.setDefaultAssertionStatus(enabled);
    }

    public void setPackageAssertionStatus(String packageName, boolean enabled) {
        check();
        super.setPackageAssertionStatus(packageName, enabled);
    }

    protected Map<String,Class> class_cache;

    public Map<String,Class> getClassCache (CallContext context) {
        return this.class_cache;
    }

    public void setClassCache (CallContext context, Map<String,Class> class_cache) {
        this.class_cache = class_cache;
    }

    protected Set<String> class_not_found_cache;

    public Set<String> getClassNotFoundCache (CallContext context) {
        return this.class_not_found_cache;
    }

    public void setClassNotFoundCache (CallContext context, Set<String> class_not_found_cache) {
        this.class_not_found_cache = class_not_found_cache;
    }

    public String toString() {
        String str = super.toString();
        str += " - Id:" + this.id + " - Active:" + this.is_active;
        return str;
    }
}
