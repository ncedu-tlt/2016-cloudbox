<%-- 
    Document   : index
    Created on : Dec 10, 2015, 7:11:32 AM
    Author     : zvyagintsev
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <title>Home</title>
    </head>
    <body>
        <c:redirect url="login"/>
        <c:choose>
            <c:when test="${empty userName}">
                <h1>Who are you?</h1>
                <p><a href="login">Login</a></p>
            </c:when>
            <c:otherwise>
                <h1>Hello ${userName}</h1>
                <p><a href="logout">Logout</a></p>
            </c:otherwise>
        </c:choose>
    </body>
</html>
