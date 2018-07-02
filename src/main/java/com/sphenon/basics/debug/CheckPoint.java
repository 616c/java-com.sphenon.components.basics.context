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

public class CheckPoint {
 
    static protected CheckPointHandler check_point_handler;

    static public CheckPointHandler getCheckPointHandler (CallContext context) {
        return check_point_handler;
    }

    static public void setCheckPointHandler (CallContext context, CheckPointHandler new_check_point_handler) {
        check_point_handler = new_check_point_handler;
    }

    protected CheckPoint(CallContext context, String id, Object... arguments) {
    }
    
    /**
     * Create Checkpoint
     *
     * param id application wide unique id of this CheckPoint, usually
     *          uniqueness is ensured by using Java dotted package name prefix
     * param arguments list of key/value pairs where key is a String
     */
    static public CheckPoint create(CallContext context, String id, Object... arguments) {
        return new CheckPoint(context, id, arguments);
    }

    protected String id;

    public String getId (CallContext context) {
        return this.id;
    }

    public void setId (CallContext context, String id) {
        this.id = id;
    }

    protected Object[] arguments;

    public Object[] getArguments (CallContext context) {
        return this.arguments;
    }

    public void setArguments (CallContext context, Object[] arguments) {
        this.arguments = arguments;
    }
}
