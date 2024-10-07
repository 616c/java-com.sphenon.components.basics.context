package com.sphenon.basics.data;

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
   A DataSource provides a complete method to obtain some target object
   without any further means (like a retrieval service, a namespace or
   alike).

   In contrast to a {@link com.sphenon.basics.reference.Reference}, the
   specific method used to obtain the object or any kind of information used
   for this retrieval is completely encapsulated and considered as being not
   of interest - while of course a Reference may serve internally as such kind
   information.
 */
public interface DataSource<T> {
    public Object getObject(CallContext context);
    public T get(CallContext context);
}
