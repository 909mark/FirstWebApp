package com.vicsimark.firstwebapp;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import com.vicsimark.firstwebapp.data.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    
    private Map<String, User> users;

    @Override
    public void init() {
        users = new HashMap<>();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        // the next 5 line is for debug
        System.out.println("Parameters in this request:");
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((entry, key) ->
                System.out.println(String.format("%s = %s",entry, Arrays.toString(key))));
        System.out.println(request.getRequestURI());

        PrintWriter out = response.getWriter();
        out.println(ResponseFactory.getOutputText(request, users));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        // I don't know what to do here
    }
}