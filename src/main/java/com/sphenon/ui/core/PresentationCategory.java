package com.sphenon.ui.core;

import com.sphenon.basics.context.*;

/*
  [ToBeImplemented:PresentationCategory - ausbauen, see PresentationCategory.java#·,VUIEquipmentInterpreter.java#·,VUIUtilities.java#·]

  naja, das hier ist im entstehen,
  [ ] klare enum values
  [ ] klare kategorien
  bspw. Extent: stammt von doclet, ist anzupassen an Object-Verhältnisse

  [ ] maybe separate (out-factor) from Extent: list (homog.) vs object (heterog.) vs. Matrix vs. Tree etc.

  Algorithmus in VUIEquipmentInterpreter:
    ggf. Vorcompilieren beim Modell-Bauen, aus basis-typen
    iterativ nach oben arbeiten, also Text + Data field in
    Object = Mixed usw.

  Ergänzend diese Infos im xmodel einstreuen, dafür ggf. für
  entweder PresentationCategory ein property, oder für dessen
  Bestandteile
*/
public enum PresentationCategory {
    DataField       (Extent.Word    , ContentType.Data),
    TextField       (Extent.Word    , ContentType.Text),
    DataObject      (Extent.Section , ContentType.Data),
    DocumentObject  (Extent.Section , ContentType.Text);

    static public enum Extent {
        Word(1),
        Phrase(2),
        Headline(3),
        Sentence(4),
        Enumeration(5),
        List(6),
        Paragraph(7),
        Section(8),
        Page(9),
        Article(10),
        Chapter(11),
        Book(12);
        Extent(int index) { this.index = index; }
        public int index;
        public Extent combine(Extent parent) {
            return   ( parent.index < index
                       ? parent
                       : this
                     );
        }           
        public boolean is(Extent... extents) {
            for (Extent extent : extents) {
                if (this == extent) { return true; }
            }
            return false;
        }
        public boolean less(Extent extent)        {  return this.index <  extent.index;  }
        public boolean more(Extent extent)        {  return this.index >  extent.index;  }
        public boolean leeq(Extent extent)        {  return this.index <= extent.index;  }
        public boolean moeq(Extent extent)        {  return this.index >= extent.index;  }
    }
    public Extent extent;

    static public enum ContentType {
        Data(1),
        Text(2),
        Mixed(3);
        ContentType(int index) { this.index = index; }
        public int index;
        public ContentType combine(ContentType parent) {
            return   ( parent.index == index
                       ? this
                       : Mixed
                     );
        }
        public boolean is(ContentType... content_types) {
            for (ContentType content_type : content_types) {
                if (this == content_type) { return true; }
            }
            return false;
        }
    }
    public ContentType content_type;

    PresentationCategory(Extent extent, ContentType content_type) {
        this.extent = extent;
        this.content_type = content_type;
    }

    static public PresentationCategory get(Extent extent, ContentType content_type) {
        switch (extent) {
            case Word: switch(content_type) {
                case Data: return DataField;
                case Text: return TextField;
            }
            case Section: switch(content_type) {
                case Data: return DataObject;
                case Text: return DocumentObject;
            }
        }
        return null;
    }

    public String toString() {
        return toString(extent, content_type);
    }

    static public String toString(Extent extent, ContentType content_type) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(extent.toString().substring(0,3));
        sb.append(",");
        sb.append(content_type.toString().substring(0,3));
        sb.append("]");
        return sb.toString();
    }
}
