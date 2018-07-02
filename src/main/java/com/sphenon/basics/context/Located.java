package com.sphenon.basics.context;

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

import com.sphenon.basics.context.classes.*;

public interface Located {
    public LocationContext getLocationContext(CallContext context);
    // yes, we do have the call context parameter here
    // since this method may need to do arbitrary complex
    // work, e.g. building up a container instance, to find it
    // (it is tempting to think this method is already an
    // exception, while only the initial contructors are)
}

// we could add here or in a derived class a method
// "getContainer", which returns a Located and is
// to be overloaded as appropriate - since it might
// be rather usual that a Located is organised in
// a hierarchy, where the "located" relation is
// inherited by such a container