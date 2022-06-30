package com.vicsimark.firstwebapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "GZIP-CompressionFilter")
public class CompressionFilter implements Filter {

    private ServletContext context; // for logging purpose
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) {
        config = filterConfig;
        context = filterConfig.getServletContext();
        context.log(config.getFilterName() + " initialized.");
    }

    @Override
    public void destroy() {
        context = null;
        config = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String validEncodings = httpRequest.getHeader("Accept-Encoding");
        if (validEncodings.contains("gzip")) {
            context.log("Valid encoding.");
            // wrap the response with an adapter that in turn wraps the output stream with a compression I/O stream
            CompressionResponseWrapper wrappedResponse =
                    new CompressionResponseWrapper(httpResponse);

            wrappedResponse.setHeader("Content-Encoding", "gzip");

            chain.doFilter(request, wrappedResponse);

            // flush the GZIP stream, send all of its data to the original response stream
            wrappedResponse.finish();

            context.log(config.getFilterName() + ": finished the request.");
        } else {
            context.log(config.getFilterName() + ": no encoding performed.");
            chain.doFilter(request, response);
        }
    }
}
