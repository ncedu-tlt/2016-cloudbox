<%-- 
    Document   : login
    Created on : Dec 10, 2015, 7:13:28 AM
    Author     : zvyagintsev
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login form</title>
    </head>
    <body>
      <form method="post" action="login" method="post">
        <p>Enter name: <input type="text" name="userName" placeholder="Username"></p>
        <p><input type="submit" name="commit" value="Login"></p>
      </form>
    </body>
</html>
