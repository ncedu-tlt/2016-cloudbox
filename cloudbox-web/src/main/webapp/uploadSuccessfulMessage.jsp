<%-- 
    Document   : uploadSuccessfulMessage
    Created on : Dec 24, 2015, Dec 24, 2015 5:42:08 PM
    Author     : victori
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload</title>
    </head>
    <body>
        <h2>${requestScope.message}</h2>

        <form action="upload.jsp">
            <button type="submit">К загрузке</button>
        </form>
    </body>
</html>
