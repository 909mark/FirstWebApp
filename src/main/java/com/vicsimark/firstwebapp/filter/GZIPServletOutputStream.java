package com.vicsimark.firstwebapp.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/*
Decorator on the ServletOutput abstract class which delegates the real work of compressing the generated
content using the standard GZIP output stream
 */
public class GZIPServletOutputStream extends ServletOutputStream {
    private final GZIPOutputStream internalGzipOS;

    GZIPServletOutputStream(ServletOutputStream servletOutputStream) throws IOException {
        this.internalGzipOS = new GZIPOutputStream(servletOutputStream);
    }

    // we DO NOT want to close the ServletOutputStream, but just the internal GZIP OS
    @Override
    public void close() throws IOException {
        System.out.println("Closing internal GZIP-OS");
        this.internalGzipOS.close();
    }

    @Override
    public void flush() throws IOException {
        System.out.println("Flushing internal GZIP-OS");
        this.internalGzipOS.flush();
    }

    public void finish() throws IOException {
        System.out.println("Finishing internal GZIP-OS");
        this.internalGzipOS.finish();
    }

    /*
    Implements the compression decoration by delegating write() call
    to the GZIP compression stream, which is wrapping the original ServletOutputStream
    NOTE: Every subclass of ServletOutputStream must implement the java.io.OutputStream.write(int) method.
     */
    @Override
    public void write(int b) throws IOException {
        System.out.println("Internal GZIP-OS:" + (char)b);
        this.internalGzipOS.write(b);
    }

    @Override
    public boolean isReady() {
        System.out.println("isReady() returns true");
        // don't know when is this called
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        System.out.println("setWriteListener() is called");
        // don't know when is this called
    }
}
