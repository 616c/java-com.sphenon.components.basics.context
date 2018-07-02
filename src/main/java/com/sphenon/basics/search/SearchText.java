package com.sphenon.basics.search;

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

import java.util.Vector;

public class SearchText {
    /**
       @see {@link Searchable#getSearchableFullText}
     */
    static public String getFullText(CallContext context, Object object) {
        if (object instanceof Searchable) {
            return ((Searchable) object).getSearchableFullText(context);
        } else if (object instanceof ContextAware) {
            return ((ContextAware) object).toString(context);
        } else {
            return object.toString();
        }
    }

    /**
       @see {@link Searchable#getSearchableTags}
     */
    public String[] getTags(CallContext context, Object object) {
        if (object instanceof Searchable) {
            return ((Searchable) object).getSearchableTags(context);
        } else {
            return null;
        }
    }

    /**
       @see {@link Searchable#getSearchableTextByTag}
     */
    public String[] getTextByTag(CallContext context, Object object, String tag) {
        if (object instanceof Searchable) {
            return ((Searchable) object).getSearchableTextByTag(context, tag);
        } else {
            return null;
        }
    }
}
