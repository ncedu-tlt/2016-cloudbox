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
        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />

        <title>Авторизация CloudBox</title>
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="app/css/login1.css" rel="stylesheet"/>
        <link href="app/css/login2.css" rel="stylesheet"/>

        <link href="app/css/login.css" rel="stylesheet"/>

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="centering text-center">

                    <div class="row">
                        <img class="profile-img" src="app/img/AuthHead.png" alt=""/>
                    </div>
                    <div class="row">

                        <form method="post" action="login" method="post">
                            <!--<div class="row " style="margin: 10px">-->
                            <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userName" name="userName" placeholder="Username" type="text">
                                </div>
                            </div>

                            <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userPass" name="userPass" placeholder="Password" type="password">
                                </div>
                            </div>
                            <div class="col-lg-3 center-block" style="float: none;">
                                    <input class="btn btn-default" type="submit" name="commit" value="Вход" >
                                    <!--<input class="btn btn-default col-lg-12"name="registr" value="Регистрация">-->
                                    <a  class="btn btn-default" href="registr.jsp">Регистрация</a>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>
</body>
</html>
