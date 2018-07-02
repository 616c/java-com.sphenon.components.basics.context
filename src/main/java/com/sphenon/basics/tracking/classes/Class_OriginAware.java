package com.sphenon.basics.tracking.classes;

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
import com.sphenon.basics.tracking.*;

public class Class_OriginAware implements OriginAware {

    protected Origin origin;

    public Origin getOrigin (CallContext context) {
        return this.origin;
    }

    public void setOrigin (CallContext context, Origin origin) {
        this.origin = origin;
    }

    static public String dumpOrigin(CallContext context, Object object) {
        if (object == null) { return ""; }
        if ((object instanceof OriginAware) == false) { return ""; }
        OriginAware origin_aware = (OriginAware) object;
        Origin origin = origin_aware.getOrigin(context);
        if (origin == null) { return ""; }
        String[] track = origin.getTrack(context);
        if (track == null) { return ""; }

        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (String string : track) {
            if (first == false) { sb.append(","); }
            first = false;
            sb.append(string);
        }
        return sb.toString();
    }
}
