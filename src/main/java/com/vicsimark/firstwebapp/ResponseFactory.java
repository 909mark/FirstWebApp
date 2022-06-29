package com.vicsimark.firstwebapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicsimark.firstwebapp.data.User;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

// is this even a "response" factory?
public class ResponseFactory {

    private static final String JSON_TYPE = "application/json";

    private ResponseFactory() {}

    public static String getOutputText(HttpServletRequest request, Map<String, User> userMap) throws IOException {

        if(!JSON_TYPE.equals(request.getHeader("content-type"))) {
            return "Not supported content-type, must be json";
        }

        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //System.out.println(requestBody);
        String operationType = request.getParameter("op-type");
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = objectMapper.readValue(requestBody, User.class);
        String username = newUser.getUsername();
        //System.out.println("new User= " + newUser);

        if (operationType == null) {
            return "No operation type specified, only add/list/delete/edit supported";
        } else if ("add".equals(operationType)) {
            if (userMap.containsKey(username)) {
                return "User already exists";
            }
            userMap.put(username, newUser);
        } else if ("delete".equals(operationType)) {
            userMap.remove(username);
        } else if ("edit".equals(operationType)) {
            if(!userMap.containsKey(username)) {
                return "No user found with username \"" + username + "\"";
            }
            userMap.remove(username);
            userMap.put(username, newUser);
        }

        String responseString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userMap.values());
        System.out.println("response =" + responseString);
        return responseString;
    }

}
