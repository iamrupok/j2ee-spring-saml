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
        //String message = (String)request.getAttribute("message");
    
    	String message = (String)request.getAttribute("saml2Response");
    %>
        <h1>SAML2 Response send From City CBOL</h1>
        
        <p><%= message %>  </p>
     
    </body>
</html>