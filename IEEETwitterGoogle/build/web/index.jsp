<%@page import="com.babatunde.twittergoogle.GoogleAuthHelper"%>
<%@page import="com.google.api.services.plusDomains.model.Circle"%>
<%@page import="com.google.api.services.plusDomains.model.CircleFeed"%>
<%@page import="com.google.api.services.plusDomains.model.ActivityFeed"%>
<%@page import="com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse"%>
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-type" content="text/html;charset=UTF-8">
        <title>Log In to Google</title>
    </head>
    <body>

        <div class="container" id="form_frame">
            <%
                final GoogleAuthHelper helper = new GoogleAuthHelper();

                if (request.getParameter("code") == null
                        || request.getParameter("state") == null) {
                    out.println("<p>Please, follow the link below to login to your account and give the application an approval.</p>");
                    out.println("<a href='" + helper.buildLoginUrl()
                            + "'>Login with your Google Account</a>");

                    session.setAttribute("state", helper.getStateToken());

                } else if (request.getParameter("code") != null
                        && request.getParameter("state") != null
                        && request.getParameter("state").equals(
                                session.getAttribute("state"))) {
                    GoogleTokenResponse resp = helper.getResponse(request.getParameter("code"));
                    PlusDomains serve = helper.getPlusDomain(resp);

                    PlusDomains.Circles.List listCircles = serve.circles().list("me");
                    listCircles.setMaxResults(100L);
                    CircleFeed circleFeed = listCircles.execute();
                    List<Circle> circles = circleFeed.getItems();

                    //session.setMaxInactiveInterval(500);
                    request.getSession().setAttribute("listCircles", circles);
                    request.getSession().setAttribute("PlusDomains", serve);

                    session.removeAttribute("state");
                    
                    out.println("<p>Thank you for logging in.</p>");
                    out.println("<p><b>To specify circle and hashtag</b></p>");
                    out.println("<a href='googletwitter.jsp'>Next</a>");
                    //request.getRequestDispatcher("googletwitter.jsp").forward(request, response);
                    
                }
            %>

        </div>
        <div id ="print"></div>
    </body>
</html>
