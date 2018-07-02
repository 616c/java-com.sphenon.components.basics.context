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

public class RuntimeStepLevel {

    // these entries are copied from notifier
    // it is not really clear if all of them
    // make sense here (e.g. PRODUCTION, CHECKPOINT)

    /**
       these steps are required for
       the system to function properly
    */
    static final public long PRODUCTION       = 0x0001; 

    /**
       important steps,
       not required for system operation,
       but highly informative
       (note: under normal circumstances, this is
       the recommended mode for production)
    */
    static final public long MONITORING       = 0x0002; 
                                                        
    /**
       can be used in production mode to
       carefully observe system operations
       e.g. in "preproduction" mode
    */
    static final public long OBSERVATION      = 0x0004; 
    
    /**
       these steps are created and can be
       relied on for automated test evaluation
    */
    static final public long CHECKPOINT       = 0x0010; 

    /**
       a convenience abbrevation, since CHECKPOINT
       and OBSERVATION do overlap, but none is a
       subset of the other
    */
    static final public long OBSERVATION_CHECKPOINT = OBSERVATION | CHECKPOINT;

                                                        
    /**
       steps for diagnostic purposes,
       considering the respective subsystem
       as a black box (external view)
    */
    static final public long DIAGNOSTICS      = 0x0020; 
                                                        
                                                        
    /**
       steps for diagnostics of the
       respective subsystem itself;
       considering it as a white box
       (internal details)
    */
    static final public long SELF_DIAGNOSTICS = 0x0040; 
    
    /**
       lots of further steps
     */
    static final public long VERBOSE          = 0x0100; 

    /**
       even more of them
     */
    static final public long MORE_VERBOSE     = 0x0200; 

    /**
       highest amount of steps;
       useful for harddisk stress testing
     */
    static final public long MOST_VERBOSE     = 0x0400; 

}
