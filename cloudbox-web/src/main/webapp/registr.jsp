<%-- 
    Document   : registr
    Created on : Dec 13, 2015, 12:46:50 AM
    Author     : Andrew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />

        <title>Регистрация в сервисе CloudBox</title>
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

                        <form method="post" action="registr" method="post">
                            
                             <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userName" name="regEmail" placeholder="e-mail" type="text">
                                </div>
                            </div>
                            
                            <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userName" name="regUserName" placeholder="Username" type="text">
                                </div>
                            </div>

                            <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userPass" name="regUserPass" placeholder="Password" type="password">
                                </div>
                            </div>
                            <div class="col-lg-2 center-block" style="float: none;" >
                                <div class="panel panel-default">


                                    <input class="form-control" id="userPass" name="regUserPass2" placeholder="reEnter Password" type="password">
                                </div>
                            </div>
                            <div class="col-lg-2 center-block" style="float: none;">
                                <div class="col-lg-6">
                                    <input class="btn btn-default col-lg-12" type="submit" name="commit" value="Регистрация" >
                                </div>
                                <div class="col-lg-6">
                                    <a  class="btn btn-default col-lg-12" href="login">Отмена</a>
                                </div>

                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>
</body>
</html>
