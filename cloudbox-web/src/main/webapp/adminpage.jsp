<%-- 
    Document   : adminpage
    Created on : Dec 13, 2015, 12:54:57 AM
    Author     : pavel.tretyakov
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

        <script language="javascript" type="text/javascript">
//---------            
            function getAllUsers()
            {
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
                    {
                        var contentTable = document.getElementById("contentTable");
                        contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                        usersList = JSON.parse(xmlhttp.responseText);
                        usersList.forEach(function (item, i, arr)
                        {
                            var d = document.createElement('tr');
                            d.innerHTML = "<td onclick=\"getUserData(" + item.USERID + ")\">" + item.USERNAME + "</td>";
                            contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                        });
                    }
                };
                xmlhttp.open("GET", "./userProcess/getAllUsers", true);
                xmlhttp.send();
            }
//---------
            function getUserData(userId)
            {
                var paramList;
                getAllRoles();
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
                    {
                        var paramsTable = document.getElementById("paramsTable");
                        paramsTable.innerHTML = '';
                        paramList = JSON.parse(xmlhttp.responseText);
                        var d = '';
                        d += '<tr><td><input onchange="updateUserData()" id="USERID" type="hidden" value="' + paramList.id + '"></td></tr>';
                        d += '<tr><td><input onchange="updateUserData()" id="USERNAME" type="text" value="' + paramList.name + '"></td></tr>';
                        d += '<tr><td><input onchange="updateUserData()" id="USERMAIL" type="text" value="' + paramList.email + '"></td></tr>';
                        d += '<tr><td><input onchange="updateUserData()" id="USERPASSHASH" type="text" value="' + paramList.hash + '"></td></tr>';
                        d += '<tr><td><input onchange="updateUserData()" id="USERNOTES" type="text" value="' + paramList.note + '"></td></tr>';
                        d += '<tr><td><input onchange="updateUserData()" id="USERPIC" type="text" value="' + paramList.picPath + '"></td></tr>';
                        paramsTable.innerHTML = d;
                        //Проставляем галочки в соответствии с ролями
                        for (var key in paramList.userRoles)
                        {
                            var elem = document.getElementById("ROLEID"+paramList.userRoles[key].id);
                            if(elem)
                            {
                                elem.checked=true;
                            }
                        }
                    }
                };
                xmlhttp.open("GET", "./userProcess/getUserData?userId=" + userId, false);
                xmlhttp.send();
            }
//---------
            function getAllRoles()
            {
                var rolesPanel = document.getElementById("rolesPanel");
                d='';
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () 
                {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        var roles = JSON.parse(xmlhttp.responseText);
                        for(var key in roles)
                        {
                            d += '<input type="checkbox" id="ROLEID'
                            +roles[key].id
                            +'" onclick="updateUserRole('
                            +roles[key].id
                            +')">'
                            +roles[key].name
                            + '<br>';
                        }
                            rolesPanel.innerHTML = d;
                    }
                };
                xmlhttp.open("GET", "./userProcess/getAllRoles", false);
                xmlhttp.send();
            }
//---------
            function updateUserData()
            {
                var userId = document.getElementById("USERID").value;
                var elem = window.event.target;
                var column = elem.id;
                var value = elem.value;
                var link = "./userProcess/updateUserData?userId=" + userId
                        + "&column=" + column
                        + "&value=" + value;
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        showAlertMessage("Данные пользователя обновлены");
                        getAllUsers();
                    }
                };
                xmlhttp.open("GET", link, true);
                xmlhttp.send();
            }
//---------
            function updateUserRole(roleId)
            {
                var elem = window.event.target;
                var value = elem.checked;
                var userId = document.getElementById("USERID").value;
                var link = "./userProcess/updateUserRole?userId=" + userId
                        + "&roleId=" + roleId
                        + "&is=" + value;
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        showAlertMessage("Роль изменена");
                        getAllUsers();
                    }
                };
                xmlhttp.open("GET", link, true);
                xmlhttp.send();
            }
//---------
            function getAllFiles()
            {
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange =
                        function ()
                        {
                            if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                            {
                                filesList = JSON.parse(xmlhttp.responseText);
                                var contentTable = document.getElementById("contentTable");
                                contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                                filesList.forEach(function (item, i, arr)
                                {
                                    var d = document.createElement('tr');
                                    d.innerHTML = "<td onclick=\"getFileData(" + item.id + ")\">" + item.name + "</td>" + "<td>" + item.ext + "</td>" + +"<td>" + item.date + "</td>";
                                    contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                                });
                            }
                        };
                xmlhttp.open("GET", "./fileProcess/getFilesList", false);
                xmlhttp.send();
            }
//---------
            function getFileData(fileId)
            {
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        var paramsTable = document.getElementById("paramsTable");
                        paramsTable.innerHTML = '';
                        document.getElementById('rolesPanel').innerHTML='';
                        paramList = JSON.parse(xmlhttp.responseText);
                        for (var key in paramList)
                        {
                            var d = document.createElement('tr');
                            d.innerHTML += "<td><input title=\"" + key + "\" type=\"text\" value=\"" + paramList[key] + "\"></td>";
                            paramsTable.appendChild(d);
                        }
                    }
                };
                xmlhttp.open("GET", "./fileProcess/getFileData?fileId=" + fileId, false);
                xmlhttp.send();
            }
//---------            
            function showAlertMessage(message){
                $('#message-text').text(message);
                $('#message-container').fadeIn('slow');
                window.setTimeout(function () {
                    $("#message-container").slideUp(750, function () {
                    });
                }, 1000);
            }
//---------
        </script>
    </head>
    
    <div class="container">
        <div class="row" id="message-container" style="display: none;">
            <div class="span12">  
                <div class = "alert alert-warning"><span id="message-text">Test Text</span></div>
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
                    <p class="btn btn-link col-lg-12" onclick="getAllUsers()">Пользователи</p>
                    <p class="btn btn-link col-lg-12" onclick="getAllFiles()">Файлы</p>
                </div>

                <div class="col-lg-6">
                    <div class="panel panel-default">
                        <table id="contentTable" class="table table-striped table-hover " cellspacing="0" width="80%">
                            <tbody>
                            </tbody>
                        </table>                      
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="panel panel-default">
                        <table id="paramsTable" class="table table-striped">
                            <tbody>
                            </tbody>
                        </table>
                        <div  id="rolesPanel">
                        </div>
                    </div>
                </div>
            </div>
        </div>



    </body>


</html>
