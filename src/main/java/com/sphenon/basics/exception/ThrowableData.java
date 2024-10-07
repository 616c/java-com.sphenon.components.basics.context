package com.sphenon.basics.exception;

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
import com.sphenon.basics.debug.*;

/** {@EntitySecurityClass User}

    @doclet {@Category Definition} {@SecurityClass User} {@Maturity Final}

    ThrowableData provides a container for arbitrary data which serves
    as an explanation of an erraneous situation. As a throwable, it
    can be attached to other Throwables as their 'root cause', or
    just thrown by themselves.
*/
public class ThrowableData extends Throwable {
// public class ThrowableData<T> extends Throwable {
// this is not allowed, because Java language designers are not humble enough
// to imagine that coders might find use cases beyond the imagination of
// themselves (in that case: they considered only the catch-issues)

    public ThrowableData(CallContext context, Object data) {
        this.data = data;
    }

    protected Object data;

    public Object getData () {
        return this.data;
    }

    public String getMessage () {
        return this.data == null ? "" : this.data.toString();
    }

    public String toString () {
        return this.data == null ? "" : this.data.toString();
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "ExceptionCause", this.data);
    }
}
