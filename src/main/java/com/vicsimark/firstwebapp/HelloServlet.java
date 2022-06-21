package com.vicsimark.firstwebapp;

import java.io.*;
import java.util.*;
import com.vicsimark.firstwebapp.data.User;
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

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(ResponseFactory.getOutputText(request, users));

        // following lines are for debugging
        System.out.println("Parameters in this request:");
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((entry, key) ->
                System.out.printf("%s = %s%n",entry, Arrays.toString(key)));
        System.out.println("URI= " + request.getRequestURI());
        System.out.println("Headers:");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            System.out.println(headers.nextElement());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        // I don't know what to do here
    }
}