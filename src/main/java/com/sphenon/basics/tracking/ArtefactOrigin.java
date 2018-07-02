package com.sphenon.basics.tracking;

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

public class ArtefactOrigin implements Origin {

    public ArtefactOrigin(CallContext context) {
    }

    public ArtefactOrigin(CallContext context, Origin base, String track_entry) {
        this(context, base, track_entry, 0L);
    }

    public ArtefactOrigin(CallContext context, Origin base, String track_entry, long last_modification) {
        String[] base_track = (base == null ? null : base.getTrack(context));
        this.track = new String[base_track == null ? 1 : base_track.length + 1];
        int i=0;
        if (base_track != null) {
            for (i=0; i<base_track.length; i++) {
                this.track[i] = base_track[i];
            }
        }
        this.track[i] = track_entry;
        this.last_modification = last_modification;
    }

    public ArtefactOrigin(CallContext context, String track_entry) {
        this(context, track_entry, 0L);
    }

    public ArtefactOrigin(CallContext context, String track_entry, long last_modification) {
        this.track = new String[1];
        this.track[0] = track_entry;
        this.last_modification = last_modification;
    }

    protected String[] track;

    public String[] getTrack (CallContext context) {
        return this.track;
    }

    public void setTrack (CallContext context, String[] track) {
        this.track = track;
    }

    protected long last_modification;

    public long getLastModification(CallContext context) {
        return last_modification;
    }
}
