<%-- 
    Document   : index
    Created on : Aug 15, 2014, 11:19:09 AM
    Author     : user
--%>
<%@page import="com.google.api.services.plusDomains.model.Circle"%>
<%@page import="com.google.api.services.plusDomains.model.CircleFeed"%>
<%@page import="com.google.api.services.plusDomains.model.ActivityFeed"%>
<%@page import="com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse"%>
<%@page import="com.babatunde.twittergoogle.GoogleAuthHelper"%>
<%@page import="com.google.api.client.json.jackson.JacksonFactory"%>
<%@page import="com.google.api.client.json.JsonParser"%>
<%@page import="com.google.api.client.json.JsonFactory"%>
<%@page import="com.google.api.services.plusDomains.model.Acl"%>
<%@page import="com.google.api.services.plusDomains.model.Activity"%>
<%@page import="com.google.api.services.plusDomains.model.PlusDomainsAclentryResource"%>
<%@page import="com.google.api.services.plusDomains.PlusDomains"%>
<%@page import="java.util.*"%>
<%@page import="java.io.IOException"%>
<%@page import="twitter4j.*"%>
<%@page import="twitter4j.StatusDeletionNotice"%>
<%@page import="twitter4j.StatusListener"%>
<%@page import="twitter4j.TwitterException"%>
<%@page import="twitter4j.TwitterStream"%>
<%@page import="twitter4j.TwitterStreamFactory"%>
<%@page import="twitter4j.conf.Configuration"%>
<%@page import="twitter4j.conf.ConfigurationBuilder"%>
<%@page import="com.babatunde.twittergoogle.Utility"%> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login To Google Plus</title>
    </head>
    <body>
        <h2>Please Fill the Form to specify the Hashtag and the Google+ Circle to Stream to</h2>
        <form autocomplete="on" action="/IEEETwitterGoogle/ChooseService" method="post">
            <table>
                <tr><td>
                        Hashtag Value:
                    </td>
                    <td>
                        <input type="text" name="hashtag" id="hashtag"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        Circle:
                    </td>
                    <td>
                        <select name="circle" id="circle">
                            <option disabled selected>Choose one department...</option>
                            <%
                                List<Circle> circles = (List<Circle>) session.getAttribute("listCircles");
                                if (circles != null) {
                                    for (Circle c : circles) {
                                        out.println("<option value='" + c.getId() + "'>" + c.getDisplayName() + "</option>");
                                    }
                                }
                            %>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="Submit"/>
                    </td></tr>
            </table>
        </form>
    </body>
</html>
