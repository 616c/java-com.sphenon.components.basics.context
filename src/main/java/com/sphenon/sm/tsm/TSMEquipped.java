package com.sphenon.sm.tsm;

/****************************************************************************
  Copyright 2001-2024 Sphenon GmbH

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

public interface TSMEquipped {

    public Object _getState(CallContext context);

    /*********************************************************************************************************************
     * IMPORTANT: derived classes are expected to implemented these static methods:
     * ----------------------------------------------------------------------------

    static public String                getPersistentTypeName     (CallContext context);

    static public PersistentType        convertToPersistentType   (CallContext context, ApplicationDomainType ad_instance);

    static public ApplicationDomainType convertFromPersistentType (CallContext context, PersistentType        sm_instance);

    **********************************************************************************************************************/
}
