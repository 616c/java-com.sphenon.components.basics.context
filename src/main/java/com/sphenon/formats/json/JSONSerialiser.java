package com.sphenon.formats.json;

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

import java.io.IOException;

public interface JSONSerialiser {
    public void serialise  (CallContext context, Object object, String name) throws IOException;
    public void openObject (CallContext context, String name) throws IOException;
    public void closeObject(CallContext context) throws IOException;
    public void openArray  (CallContext context, String name) throws IOException;
    public void closeArray (CallContext context) throws IOException;

    public int  getCurrentLevel(CallContext context);

    public boolean getProperty(CallContext context, String name, boolean default_value);
    public int     getProperty(CallContext context, String name, int default_value);
    public String  getProperty(CallContext context, String name, String default_value);
    public String  setProperty(CallContext context, String name, String value);

    public Object getAttachment(CallContext context, String name);
    public void   setAttachment(CallContext context, String name, Object attachment);

}
