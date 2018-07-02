package com.sphenon.basics.testing.classes;

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

import com.sphenon.basics.testing.*;

import java.util.Map;
import java.util.HashMap;

public class ClassTestRun implements TestRun {

    public ClassTestRun (CallContext context) {
        this(context, null);
    }

    public ClassTestRun (CallContext context, String id) {
        this.id = (id != null ? id : (new java.text.SimpleDateFormat ("yyyyMMddHHmmss")).format(new java.util.Date()));
    }

    public ClassTestRun (CallContext context, String id, String test_filter_include_reg_exp, String test_filter_exclude_reg_exp) {
        this(context, id);
        this.test_filter_include_reg_exp = test_filter_include_reg_exp;
        this.test_filter_exclude_reg_exp = test_filter_exclude_reg_exp;
    }

    protected String id;

    public String getId (CallContext context) {
        return this.id;
    }

    public void setId (CallContext context, String id) {
        this.id = id;
    }

    protected Map<String,Object> values;

    public Object getValue (CallContext context, String name) {
        if (this.values == null) { return null; }
        return this.values.get(name);
    }

    public void setValue (CallContext context, String name, Object value) {
        if (this.values == null) {
            this.values = new HashMap<String,Object>();
        }
        this.values.put(name, value);
    }

    protected String test_filter_include_reg_exp;

    public String getTestFilterIncludeRegExp (CallContext context) {
        return this.test_filter_include_reg_exp;
    }

    public void setTestFilterIncludeRegExp (CallContext context, String test_filter_include_reg_exp) {
        this.test_filter_include_reg_exp = test_filter_include_reg_exp;
    }

    protected String test_filter_exclude_reg_exp;

    public String getTestFilterExcludeRegExp (CallContext context) {
        return this.test_filter_exclude_reg_exp;
    }

    public void setTestFilterExcludeRegExp (CallContext context, String test_filter_exclude_reg_exp) {
        this.test_filter_exclude_reg_exp = test_filter_exclude_reg_exp;
    }

    public boolean matchesTest(CallContext context, Test test) {
        String path = test == null ? "" : test.getPath(context) == null ? "" : test.getPath(context);
        boolean matches = (    (    this.test_filter_include_reg_exp == null
                                 || path.matches(this.test_filter_include_reg_exp) == true
                               )
                            && (    this.test_filter_exclude_reg_exp == null
                                 || path.matches(this.test_filter_exclude_reg_exp) == false
                               )
                          );
        if (matches /* || true */) {
            System.err.println("Test " + path + " " + (matches ? "selected" : "skipped"));
        }
        return matches;
    }
}
