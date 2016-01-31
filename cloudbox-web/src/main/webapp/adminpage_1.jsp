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
        <title>Администрирование</title>

        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">

        <link href="app/css/adminpage.css" rel="stylesheet">

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
        <script src="app/js/adminpage.js"></script>
    </head>

    <div id="alertMessage" class="container">
        <div class="row" id="message-container" style="display: none;">
            <div class="span12">  
                <div class = "alert alert-success"><span id="message-text">Test Text</span></div>
            </div>
        </div>
    </div>


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
                            <c:set var="theString" value="${userroles}"/>
                            <c:if test="${fn:contains(theString,'2')||fn:contains(theString,'3')}">
                                <!--<li class="active"><a href="#">Мой диск<span class="sr-only">(current)</span></a></li>-->
                                <li ><a href="drive.jsp">Мой диск<span class="sr-only">(current)</span></a></li>
                                </c:if>

                            <li class="active"><a href="adminpage_1.jsp">Администрирование</a></li>

                        </ul>

                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${userName} <span class="caret"></span></a>
                                <ul class="dropdown-menu" role="menu">
                                    <li><a href="usersettings">Настройки</a></li>

                                    <li class="divider"></li>
                                    <li><a href="logout">Выход</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>


            <div class="row">
                <div class="col-lg-6">
                    <div class="panel panel-default">
                        <div id="usersPanelHead" class="panel-heading">Пользователи</div>
                        <div id="rolesButtons" class="btn-group btn-group-justified">
                        </div>
                        <table id="usersTable" class="table table-hover " cellspacing="0" width="100%">
                        </table>                      
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="panel panel-default">
                        <div id="filesPanelHead" class="panel-heading">
                            Файлы
                        </div>
                        <div class="">
                            <div class="btn-group btn-group-justified">
                                <a class="btn" onclick="getAllFiles('all')"> Все </a>
                            </div>
                        </div>
                        <table id="filesTable" class="table table-hover " cellspacing="0" width="100%">
                        </table>                      
                    </div>
                </div>
            </div>
        </div>

        <div id="popup" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="popupTitle" class="modal-title"></h4>
                    </div>
                    <div id="properties" class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                    </div>
                </div>
            </div>
        </div>

    </body>


</html>
