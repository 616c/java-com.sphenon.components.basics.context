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

abstract public class ThreadWithContext extends Thread {

    private CallContext context;

    protected CallContext getContext () {
        if (this.context == null) {
            this.context = this.getThreadContext().getContext();
        }
        return this.context;
    }

    public ThreadWithContext (ThreadContext thread_context) {
        this.setThreadContext(thread_context);
    }

    protected ThreadContext thread_context;

    public ThreadContext getThreadContext () {
        return this.thread_context;
    }

    public void setThreadContext (ThreadContext thread_context) {
        this.thread_context = thread_context;

        ClassLoader cl = this.getThreadContext().getClassLoader();
        this.setContextClassLoader(cl);
    }
}
