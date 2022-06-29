package com.vicsimark.firstwebapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "RequestFilter")
public class RequestFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        filterConfig.getServletContext().log(filterConfig.getFilterName() + " initialized.");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userAgent = httpRequest.getHeader("User-Agent");

        if(userAgent != null) {
            filterConfig.getServletContext().log("User-agent " + userAgent + " registered");
        }

        chain.doFilter(request, response);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    }
}
