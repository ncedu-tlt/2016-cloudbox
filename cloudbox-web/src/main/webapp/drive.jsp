<%-- 
    Document   : app
    Created on : Dec 11, 2015, 2:07:04 AM
    Author     : Andrew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cloudbox</title>
        
        <link rel="import" href="app/HTMLtemplates/sharedFileRightPanel.html">
        
        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">

        <link href="app/css/drive.css" rel="stylesheet">

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
        
        <!-- about loader -->
        <script src="http://malsup.github.com/jquery.form.js"></script>
        <!-- about table -->
        <link href="https://cdn.datatables.net/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet"/>
        <script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.10/js/jquery.dataTables.min.js"></script>

        
        <script src="app/js/userFilePage.js"></script>
        <script>
            preparePage ();
        </script>        
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
                            <li class="active"><a href="#">Мой диск<span class="sr-only">(current)</span></a></li>
                            <!--                            <li class="active">-->


                            <!--                                <div class="col-lg-12" style="margin-top: 8px">
                                                                <select class="form-control" id="select">
                            
                                                                    <option> Мой диск</option>
                                                                    <option>Диск пользователя 1</option>
                                                                    <option>Диск пользователя 2</option>
                                                                    <option>Диск пользователя 3</option>
                                                                    <option>Диск пользователя 4</option>
                                                                    <option>Диск пользователя 5</option>
                                                                </select>
                                                            </div>-->

                            <!--</li>-->
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

            <div class="row">
                <div class="col-lg-2" id="leftButtonPanel">
<!--                    <a href="#" class="btn btn-danger col-lg-12">Загрузить файл <i class="fa fa-cloud-upload"></i></a>   -->
                    <a href="#" class="btn btn-link col-lg-12" id="toOwnedFilesList">Мои файлы</a>
                    <a href="#" class="btn btn-link col-lg-12" id="toSharedFilesList">Доступные мне</a>
                    <a href="#" class="btn btn-link col-lg-12" id="toGarbageFilesList">Корзина <i class="fa fa-trash"></i></a>
                </div>



                <div class="col-lg-8">
                    <div class="panel panel-default">                      
                        <table id="mainTable" class="display" cellspacing="0" width="100%">
                            <thead>                                
                                <tr>
                                    <th>Имя</th>
                                    <th>Расширение</th>
                                    <th>Дата</th>
                                </tr>
                            </thead>
                            <tbody>  
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                            </tbody>
                        </table>   
                        <!--</div>--> 
                    </div>
                </div>

                <div class="col-lg-2" id="rightButtonPanel">
 
                </div>

            </div>

        </div>


    </body>
    <template  id="ownFileRightPanel">
                    <form id="loader" onsubmit="loadFileToServer()" method="post" enctype="multipart/form-data">
                        <input type="file" name="file"/>
                        <input type="submit"/>
                    </form>                    
                    <p></p>
                    <a href="#" id="getFileButton" class="btn btn-success col-lg-12">Скачать <i class="fa fa-cloud-download"></i></a>                   
                    <p></p>
                    <a href="#" id="putToGarbageButton" class="btn btn-warning col-lg-12">Удалить <i class="glyphicon glyphicon-remove"></i></a>
                    <a href="TestFileServlet" id="hidden_link_get_file" download hidden></a>
                    <a href="#" id="shareButton" class="btn btn-primary col-lg-12">Поделиться <i class="glyphicon glyphicon-share-alt"></i></a>
                    <a href="#" id="deleteLinkButton" class="btn btn-primary col-lg-12">Создать ссылку <i class="glyphicon glyphicon-share"></i></a>

                            <div class="form-group">
                                <label for="downloadLink" class="col-lg-12 control-label">Ссылка на файл</label>
                                <div class="col-lg-12"style="padding: 0px">
                                    <input type="text" class="form-control" id="downloadLink" placeholder="downloadLink">
                                </div>
                            </div>  
    </template>
    <template  id="sharedFileRightPanel">
                    <a href="#" id="getFileButton" class="btn btn-success col-lg-12">Скачать <i class="fa fa-cloud-download"></i></a>                   
                    <p></p>
                    <a href="#" id="deleteLinkButton" class="btn btn-warning col-lg-12">Удалить шару<i class="glyphicon glyphicon-remove"></i></a>  
    </template>
    <template  id="garbageFilesRightPanel">
                    <a href="#" id="restoreFileButton" class="btn btn-success col-lg-12">Восстановить <i class="fa fa-cloud-download"></i></a>                   
                    <p></p>
                    <a href="#" id="deleteButton" class="btn btn-warning col-lg-12">Удалить <i class="glyphicon glyphicon-remove"></i></a>  
    </template>
</html>
