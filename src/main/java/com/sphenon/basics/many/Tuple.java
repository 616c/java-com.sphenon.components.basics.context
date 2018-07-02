package com.sphenon.basics.many;

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

public class Tuple<T> implements ContextAware {
    private T[] items;

    public Tuple(CallContext context, T... items) {
        this.items = items;
    }

    public Tuple(CallContext context, Vector<T> vector, Class item_class) {
        T[] array = (T[]) java.lang.reflect.Array.newInstance(item_class, vector == null ? 0 : vector.size());
        this.items = vector == null ? array : vector.toArray(array);
    }

    public Tuple(CallContext context, Vector vector) {
        this(context, vector, Object.class);
    }

    public T getItem(CallContext context, int index) { return this.items[index]; }
    
    public void setItem(CallContext context, int index, T item) { this.items[index] = item; }

    public T[] getItems(CallContext context) { return this.items; }
    
    public void setItems(CallContext context, T... items) { this.items = items; }

    public boolean equals(Object object) {
        if ((object instanceof Tuple) == false) { return false; }
        Tuple<T> other = (Tuple<T>) object;
        if (this.items == null && other.items == null) { return true; }
        if (this.items != null && other.items == null) { return false; }
        if (this.items == null && other.items != null) { return false; }
        if (this.items.length != other.items.length) { return false; }
        for (int i=0; i<this.items.length; i++) {
            if (this.items[i].equals(other.items[i]) == false) { return false; }
        }
        return true;
    }

    public int hashCode() {
        int hc = 0;
        if (this.items != null) {
            for (int i=0; i<this.items.length; i++) {
                hc ^= this.items[i].hashCode();
            }
        }
        return hc;
    }

    public String toString(CallContext context) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        sb.append("[");
        if (this.items != null) {
            for (int i=0; i<this.items.length; i++) {
                if (first) { first = false; } else { sb.append(","); }
                sb.append(ContextAware.ToString.convert(context, this.items[i]));
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
