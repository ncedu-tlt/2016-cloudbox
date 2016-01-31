<%-- 
    Document   : adminpage
    Created on : Dec 13, 2015, 12:54:57 AM
    Author     : pavel.tretyakov
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Настройки пользователя</title>

        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">

        <link href="app/css/adminpage.css" rel="stylesheet">

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>


        <script src="app/js/usersettings.js"></script>



    </head>




    <body>



        <div class="container">
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="#" style="padding: 5px">
                            <img alt="Brand" src="app/img/ico.png" style="width: 55px">
                        </a>
                    </div>

                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <li ><a href="drive.jsp">Мой диск<span class="sr-only">(current)</span></a></li>

                            <c:set var="theString" value="${userroles}"/>
                            <c:if test="${fn:contains(theString, '1')}">
                                <li><a href="adminpage.jsp">Администрирование</a></li>
                                </c:if>

                        </ul>

                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${userName} <span class="caret"></span></a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="usersettings.jsp">Настройки</a></li>

                                    <li class="divider"></li>
                                    <li><a href="logout">Выход</a></li>
                                </ul>
                            </li>
                        </ul>


                    </div>
                </div>
            </nav>

            <!--            <div class="row">
            
                            <div class="col-lg-2">
                                <div class="btn btn-link col-lg-12" onclick="getAllUsers()">Пользователи</div>                   
                            </div>
            
                            <div class="col-lg-4">
                                <div class="panel panel-default">
                                    <table id="contentTable" class="table table-striped table-hover " cellspacing="0" width="80%">
                                        <tbody>
                                        </tbody>
                                    </table>                      
                                </div>
                            </div>
            
                            <div class="col-lg-6">
                                <div id="properties" class="panel panel-default">
                                </div>
                            </div>
                        </div>-->

            <div class="row">

                <form method="post" action="usersettings" method="post">

                    <div class="col-lg-6 center-block" style="float: none;" >
                        E-mail:
                        <div class="panel panel-default">
                            <c:choose>
                                <c:when test="${not empty usermail}">
                                    <input class="form-control" id="regEmail" name="regEmail" placeholder="e-mail" type="text" value="${usermail}">
                                </c:when>
                                <c:otherwise>
                                   <input class="form-control" id="regEmail" name="regEmail" placeholder="e-mail" type="text">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="col-lg-6 center-block" style="float: none;" >
                        Новый пароль:
                        <div class="panel panel-default">


                            <input class="form-control" id="regEmail" name="regPass" placeholder="password" type="text">
                        </div>
                    </div>

                    <div class="col-lg-6 center-block" style="float: none;">
                        <div class="col-lg-6">
                            <input class="btn btn-default col-lg-12" type="submit" name="commit" value="Сохранить изменения" >
                        </div>


                    </div>



                </form>
                <!--                        <div class="col-lg-2 center-block" style="float: none;" style="margin-top: 50px">
                <c:if test="${not empty message}">
                    <div class="alert alert-dismissible alert-danger">
                        <button type="button" class="close" data-dismiss="alert">X</button>
                        <strong>${message}</strong>
                    </div>
                </c:if>
            </div>-->
            </div>

        </div>



    </body>


    <div class="container">
        <div class="row" id="message-container" style="display: none;">
            <div class="span12">  
                <div class = "alert alert-warning"><span id="message-text">Test Text</span></div>
            </div>
        </div>
    </div>

</html>
