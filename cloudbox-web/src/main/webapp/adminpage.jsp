<%-- 
    Document   : adminpage
    Created on : Dec 13, 2015, 12:54:57 AM
    Author     : pavel.tretyakov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Администрирование</title>
        
         <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">


        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
        
        <script language="javascript" type="text/javascript">
            function getAllUsers()
            {
            xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = 
            function()
            {
                if(xmlhttp.readyState == 4 && xmlhttp.status == 200)
                {
                    console.log(xmlhttp.responseText);
                    usersList = JSON.parse(xmlhttp.responseText);
                    document.getElementById("contentTable").getElementsByTagName("tbody")[0].innerHTML = "";
                    usersList.forEach(function(item, i, arr){
                        var d = document.createElement('tr');
                        console.log(item.id);
                                    d.innerHTML = "<td>" + item.name + "</td>" + "<td>" + item.ext + "</td>" + +"<td>" + item.date + "</td>";
                                    document.getElementById("contentTable").getElementsByTagName("tbody")[0].appendChild(d);
                                });
                            }
                        };
                xmlhttp.open("GET", "http://localhost:8080/cloudbox-web/fileProcess/getFilesList", true);
                xmlhttp.send();
            }
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
                            <li ><a href="drive.jsp">Мой диск<span class="sr-only">(current)</span></a></li>
                            
                            <li class="active"><a href="adminpage.jsp">Администрирование</a></li>

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
                
                <div class="col-lg-2">
                    <a href="#" class="btn btn-link col-lg-12" onclick="getAllUsers()">Пользователи</a>
                    <a href="#" class="btn btn-link col-lg-12" onclick="getAllFiles()">Файлы</a>
                </div>
                
                <div class="col-lg-8">
                    <div class="panel panel-default">
                        <table id="contentTable" class="table table-striped table-hover " cellspacing="0" width="80%">
                            <thead>
                                <tr>
                                    <th>Заголовок раз</th>
                                    <th>Второй заголовок</th>
                                    <th>Последний заголовок</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Котики</td>
                                    <td>.jpg</td>
                                    <td>11.11.2015</td>
                                </tr>
                                <tr>
                                    <td>Владимирский централ</td>
                                    <td>.mp3</td>
                                    <td>10.10.2014</td>
                                </tr>
                                <tr>
                                    <td>ХХХ</td>
                                    <td>.doc</td>
                                    <td>20.06.2015</td>
                                </tr>
                            </tbody>
                        </table>                      
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
