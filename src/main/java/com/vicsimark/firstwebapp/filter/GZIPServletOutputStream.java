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
    // It is package-private to allow the compression response wrapper access to this variable.
    GZIPOutputStream internalGzipOS;

    GZIPServletOutputStream(ServletOutputStream servletOutputStream) throws IOException {
        internalGzipOS = new GZIPOutputStream(servletOutputStream);
    }

    /*
    Implements the compression decoration by delegating the write() call
    to the GZIP compression stream, which is wrapping the original ServletOutputStream
     */
    @Override
    public void write(int param) throws IOException {
        this.internalGzipOS.write(param);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }
}
