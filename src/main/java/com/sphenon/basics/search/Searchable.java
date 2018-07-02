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

public interface Searchable {

    /**
       Returns a (possibly long) text containing all data to
       be used in a full text search

       @return All available text, not necessarily well formatted for humans 
    */
    public String getSearchableFullText(CallContext context);

    /**
       Returns a list of tags for parts of classified text. Typical tags are
       "keyword", "title", "description", "type". It is wise to stick to these
       as far as possible.

       @return A list of tags
    */
    public String[] getSearchableTags(CallContext context);

    /**
       Returns a list of texts which are tagged with the respective tag.
       Typically "title" will return a single entry, while "keyword" many.

       @return A list of texts
    */
    public String[] getSearchableTextByTag(CallContext context, String tag);
}
