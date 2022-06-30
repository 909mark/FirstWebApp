package com.vicsimark.firstwebapp.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/*
This response wrapper decorates the original response object by adding a compression
decorator on the original servlet output stream
 */
public class CompressionResponseWrapper extends HttpServletResponseWrapper {
    private GZIPServletOutputStream servletGzipOS;
    private PrintWriter printWriter; // PrintWriter object for the compressed output stream

    CompressionResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /*
    Gives the compression filter a handle on the GZIP output stream
    so that the filter can "finish" and flush the GZIP stream
     */

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        System.out.println("getOutputStream() is called");
        /*
        Allow the servlet to access output stream, only if
        the servlet has not already accessed the PrintWriter
         */
        if (this.printWriter != null) {
            throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
        }

        /*
        Wrap the original servlet output stream with the
        adapter compression servlet OS
         */
        if(servletGzipOS == null) {
            servletGzipOS = new GZIPServletOutputStream(getResponse().getOutputStream());
        }

        return servletGzipOS;
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        System.out.println("getWriter() is called");
        /*
        Allow the servlet to access the PrintWriter, only if
        the servlet has not already accessed the servlet output stream
         */
        if (this.printWriter == null && this.servletGzipOS != null) {
            throw new IllegalStateException("OutputStream obtained already - cannot get PrintWriter");
        }
        if(this.printWriter == null) {
            ServletResponse response = getResponse();
            // wrap the servlet output stream and then
            // wrap the compression servlet OS in two additional OS decorators
            servletGzipOS = new GZIPServletOutputStream(response.getOutputStream());
            OutputStreamWriter outputStreamWriter = // converts characters into bytes
                    new OutputStreamWriter(servletGzipOS, response.getCharacterEncoding());
            this.printWriter = new PrintWriter(outputStreamWriter);

        }
        return this.printWriter;
    }

    @Override
    public void setContentLength(int len) {
        //ignore, since content length of zipped content
        //does not match content length of unzipped content.
    }

    public void finish() throws IOException {
        if(this.printWriter != null) {
            System.out.println("Closing PrintWriter in CompressionResponseWrapper");
            /*
            What happens here is a long story.

            PrintWriter calls the close() method, which calls its wrapped (a Writer) object's close method.

            In this case, the Writer object is an OutputStreamWriter instance (an OutputStreamWriter is a bridge
            from character streams to byte streams: Characters written to it are encoded into bytes using
            a specified charset). Every OutputStreamWriter object has a reference to a StreamEncoder object -
            another subclass of Writer class - and basically each write/close/flush method call is applied on
            this StreamEncoder object - just another wrapping. OutputStreamWriter gets its reference to
            the StreamEncoder object via a factory pattern method from StreamEncoder (converts an OutputStream
            into a StreamEncoder).

            When the application calls the StreamEncoder object's close() method,
            it flushes and finally (in try-catch sense) closes the OutputStream object. This is when the
            close() and flush() methods, overridden in GZIPServletOutputStream class, are called.

            In our case, the OutputStream is a GZIPServletOutputStream, wrapper of the GZIPOutputStream, subclass
            of ServletOutputStream - another subclasses of OutputStream. The thing is, that ServletOutputStream just
            inherits the unimplemented close/flush methods from OutputStream superclass. THIS IS WHY we have
            to override them and specify that we want to close/flush the wrapped GZIP output stream .

            PrintWriter.close() calls it's Writer.close()
                -> OutputStreamWriter.close() calls it's StreamEncoder.close()
                    -> StreamEncoder.close() calls it's OutputStream.flush() and OutputStream.close()
                        -> GZIPServletOutputStream.close() calls it's GZIPOutputStream.close()
                            -> GZIPOutputStream.close() inherits DeflaterOutputStream.close() method
                                -> DeflaterOutputStream.close() calls it's inherited OutputStream objects close() method
                                    -> The OutputStream object is a ServletOutputStream, it is closed by the container
             */
            this.printWriter.close();
            System.out.println("Closed PrintWriter in CompressionResponseWrapper");
        }else if(this.servletGzipOS != null){
            // when do we enter this branch?
            this.servletGzipOS.finish();
            System.out.println("Finished GZIP-ServletOS in CompressionResponseWrapper");
        }
    }
}
