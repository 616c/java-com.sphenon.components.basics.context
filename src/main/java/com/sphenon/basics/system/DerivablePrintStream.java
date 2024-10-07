package com.sphenon.basics.system;

import java.io.*;
import java.util.Locale;

abstract public class DerivablePrintStream extends PrintStream {

    static protected OutputStream dummy = new ByteArrayOutputStream();
    // only because PrintStream needs something for initialisation
    // since it's no interface but already a class with an implementation
    // (guys, study OO a little before you use it, really)
    // if this is not provided -> null pointer exception

    public DerivablePrintStream() {
        super(dummy);
    }

    public PrintStream append(char c) {
        print((new Character(c)).toString());
        return this;
    }
    public PrintStream append(CharSequence csq) {
        print(csq.toString());
        return this;
    }
    public PrintStream append(CharSequence csq, int start, int end) {
        print(csq.subSequence(start, end).toString());
        return this;
    }
    public boolean checkError() {
        return false;
    }
    protected void clearError() {
    }
    public void close() {
    }
    public void flush() {
    }
    public PrintStream format(Locale l, String format, Object... args) {
        print(String.format(l, format, args));
        return this;
    }
    public PrintStream format(String format, Object... args) {
        print(String.format(format, args));
        return this;
    }
    public void print(boolean b) {
        print((new Boolean(b)).toString());
    }
    public void print(char c) {
        print((new Character(c)).toString());
    }
    public void print(char[] s) {
        print(new String(s));
    }
    public void print(double d) {
        print((new Double(d)).toString());
    }
    public void print(float f) {
        print((new Float(f)).toString());
    }
    public void print(int i) {
        print((new Integer(i)).toString());
    }
    public void print(long l) {
        print((new Long(l)).toString());
    }
    public void print(Object obj) {
        print(obj.toString());
    }
    public void print(String s) {
    }
    public PrintStream printf(Locale l, String format, Object... args) {
        print(String.format(l, format, args));
        return this;
    }
    public PrintStream printf(String format, Object... args) {
        print(String.format(format, args));
        return this;
    }
    public void println() {
        print("\n");
    }
    public void println(boolean x) {
        print(x);
        println();
    }
    public void println(char x) {
        print(x);
        println();
    }
    public void println(char[] x) {
        print(x);
        println();
    }
    public void println(double x) {
        print(x);
        println();
    }
    public void println(float x) {
        print(x);
        println();
    }
    public void println(int x) {
        print(x);
        println();
    }
    public void println(long x) {
        print(x);
        println();
    }
    public void println(Object x) {
        print(x);
        println();
    }
    public void println(String x) {
        print(x);
        println();
    }
    protected void setError() {
    }
    public void write(byte[] buf, int off, int len) {
        print(new String(buf, off, len));
    }
    public void write(int b) {
        print(b);
    }
}
