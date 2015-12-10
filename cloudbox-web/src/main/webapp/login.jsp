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
        <title>Авторизация Cloud Box</title>
        <link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="app/css/login1.css" rel="stylesheet"/>
        <link href="app/css/login2.css" rel="stylesheet"/>

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="row">
            <img class="profile-img" src="app/img/AuthHead.png" alt=""/>
        </div>
      

                <div class="row">
                    <form method="post" action="login" method="post">
                        <div class="row ">
                            <div class="col-lg-2 center-block">
                                <input class="form-control" id="userName" name="userName" placeholder="Username" type="text">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-2 center-block">
                                <input class="form-control" id="userPass" name="userPass" placeholder="Password" type="password">
                            </div>
                        </div>
                        <div class="col-lg-2 center-block">
                            <input class="btn btn-default col-lg-4" type="submit" name="commit" value="Вход">
                            <input class="btn btn-default col-lg-4" name="cancel" value="Отмена">
                            <input class="btn btn-default col-lg-4" name="registr" value="Регистрация">
                        </div>
        
                    </form>
                </div>
    </body>
</html>
