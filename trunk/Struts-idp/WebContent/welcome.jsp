<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Welcome page | Hello World Struts application in Eclipse</title>
    </head>
    <body>
    <%
        String message = (String)request.getAttribute("message");
        String contextPath = request.getContextPath();
    %>
        <%-- <h1>Welcome <%= message %>    <%= contextPath %> at Citi CBOL</h1> --%>
        <h1>Welcome <%= message %> at Citi CBOL</h1>
        
         <p>Log In to Thank You Rewards  </p><a href="<%= contextPath %>/loginSeamless1.jspx"> Thank You Rewards </a> 
         
     
    </body>
</html>