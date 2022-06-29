package com.vicsimark.firstwebapp.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

/*
This response wrapper decorates the original response object by adding a compression
decorator on the original servlet output stream
 */
public class CompressionResponseWrapper extends HttpServletResponseWrapper {
    private GZIPServletOutputStream servletGzipOS;
    private PrintWriter out; // PrintWriter object for the compressed output stream
    private Object streamUsed;

    CompressionResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /*
    Gives the compression filter a handle on the GZIP output stream
    so that the filter can "finish" and flush the GZIP stream
     */
    public GZIPOutputStream getGZIPOutputStream() {
        return servletGzipOS.internalGzipOS;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        /*
        Allow the servlet to access output stream, only if
        the servlet has not already accessed the PrintWriter
         */
        if((streamUsed != null) && (streamUsed != out)) {
            throw new IllegalStateException();
        }

        /*
        Wrap the original servlet output stream with the
        adapter compression servlet OS
         */
        if(servletGzipOS == null) {
            servletGzipOS = new GZIPServletOutputStream(getResponse().getOutputStream());
            streamUsed = servletGzipOS;
        }

        return servletGzipOS;
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        /*
        Allow the servlet to access the PrintWriter, only if
        the servlet has not already accessed the servlet output stream
         */
        if((streamUsed != null) && (streamUsed != servletGzipOS)) {
            throw new IllegalStateException();
        }
        ServletResponse response = getResponse();
        if(out == null) {
            // wrap the servlet output stream and then
            // wrap the compression servlet OS in two additional OS decorators
            servletGzipOS = new GZIPServletOutputStream(response.getOutputStream());
            OutputStreamWriter outputStreamWriter = // converts characters into bytes
                    new OutputStreamWriter(servletGzipOS, response.getCharacterEncoding());
            out = new PrintWriter(outputStreamWriter);
            streamUsed = out;
        }
        return out;
    }

}
