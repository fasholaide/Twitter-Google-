<%-- 
    Document   : stream
    Created on : Aug 15, 2014, 1:26:58 PM
    Author     : user
--%>

<%@page import="com.google.api.services.plusDomains.PlusDomains"%>
<%@page import="com.babatunde.twittergoogle.Utility"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Streaming Tweets</title>
    </head>
    <body>
        <h1>Yes!</h1>
        <%
            String circle = (String) request.getSession().getAttribute("circle");
            String hashtag = (String) request.getSession().getAttribute("hashTAG");
            PlusDomains domain = (PlusDomains) request.getSession().getAttribute("PlusDomains");

            new Utility().postToGoogle(domain, circle, (hashtag.startsWith("#") ? hashtag : "#" + hashtag));
            out.println("<p>Congratulations, You are now streaming into " + circle + " Circle with hashtag "
                    + (hashtag.startsWith("#") ? hashtag : "#" + hashtag) + "</p>");
        %>
    </body>
</html>
