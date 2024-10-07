package com.sphenon.basics.format;

import com.sphenon.basics.context.*;

public class Formatter {

    public interface FormatService {
        public String format(CallContext context, String format, Object value);
    }

    static public FormatService formatter;

    static public String format(CallContext context, String format, Object value) {
        return formatter.format(context, format, value);
    }

    public interface ParseService {
        public Object parse(CallContext context, String format, String string);
    }

    static public ParseService parser;

    static public Object parse(CallContext context, String format, String string) {
        return parser.parse(context, format, string);
    }
}
