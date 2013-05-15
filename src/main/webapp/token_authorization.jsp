<%@ page import="java.util.List, java.io.IOException, javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse" %>

<%

    String consumer_id = (String)request.getAttribute("oauth_consumer_id");
    String consumer_display = (String)request.getAttribute("oauth_consumer_display");
    String confirm_uri = (String)request.getAttribute("oauth_token_confirm_uri");
    String request_token = (String)request.getAttribute("oauth_request_token");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Consumer Request Token Authorization</title>
</head>
<body>
<h1>Consumer Request Token Authorization</h1>
<em></em>
<p>
<strong>Consumer:</strong> <pre><%= consumer_id%></pre><br>
<form name="token-authorization" action="<%= confirm_uri %>" method="POST">

   <input type="hidden" name="xoauth_end_user_decision" value="yes"/>
   <input type="hidden" name="oauth_token" value="<%= request_token %>"/>
   
   <button type="submit">Click to authorize</button>
</form>
</body>
</html>