package com.vicsimark.firstwebapp;

import com.vicsimark.firstwebapp.data.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class ResponseFactory {

    // is this even a "response" factory?
    private ResponseFactory() {}
    public static String getOutputText(HttpServletRequest request, Map<String, User> userMap) {

        String operationType = request.getParameter("op-type");
        String username = request.getParameter("username");

        if (operationType == null) {
            return "Unknown operation type, only add/list/delete/edit supported";
        } else if ("add".equals(operationType)) {
            if (userMap.containsKey(username)) {
                return "User already exists";
            }
            userMap.put(username, new User(
                    username,
                    request.getParameter("property1"),
                    request.getParameter("property2"),
                    request.getParameter("property3")
            ));
        } else if ("delete".equals(operationType)) {
            userMap.remove(request.getParameter("username"));
        } else if ("edit".equals(operationType)) {
            if(!userMap.containsKey(username)) {
                return "No user found with username \"" + username + "\"";
            }
            userMap.remove(username);
            userMap.put(username, new User(
                    username,
                    request.getParameter("property1"),
                    request.getParameter("property2"),
                    request.getParameter("property3")
            ));
        }

        StringBuilder outputText = new StringBuilder();
        userMap.forEach((key, entry) -> outputText.append(entry).append("\n"));
        return outputText.toString();
    }

}
