<%-- 
    Document   : upload
    Created on : Dec 24, 2015, Dec 24, 2015 5:40:10 PM
    Author     : victori
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Загрузить файл</title>
    </head>
    <body>
    <center>
        <h1>Загрузить файл</h1>
        <form method="post" action="UploadServlet"
              enctype="multipart/form-data">
            Выберите файл для загрузки: <input type="file" name="file" size="60" /><br />
            <br /> <input type="submit" value="Загрузить" />
        </form>
    </center>
</body>
</html>
