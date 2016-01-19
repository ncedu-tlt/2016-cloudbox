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
            var rolesList = getAllRoles();
            $(document).ready(function(){
                for(var key in rolesList)
                {
                    $('#menu').append('<li onclick = getUsersByRole('+rolesList[key].id+')><a>'+rolesList[key].name+'</a></li>');
                };
            });
            
//---------            
            function showUserProperties(userId)
            {
                var paramList = getUserData(userId);
                var propertiesDiv=document.getElementById('properties');
                propertiesDiv.innerHTML='';
                var elem=propertiesDiv.appendChild(document.createElement('div'));
                elem.id='rolesPanel';
                var rolesString='';
                for(var key in rolesList)
                {
                    rolesString += '<input type="checkbox" id="ROLEID'
                    +rolesList[key].id
                    +'" onclick="updateUserRole('
                    +rolesList[key].id
                    +')">'
                    +rolesList[key].name
                    + '<br>';
                }
                elem.innerHTML = rolesString;
                elem = propertiesDiv.insertBefore(document.createElement('div'), elem);
                elem.id='paramsTable';
                var infoHTML = '<img alt="фотка персонажа" class="img-rounded" src="' + paramList.picPath + '"></br>';
                infoHTML += '<span class="param">UID</span>';
                infoHTML += '<input disabled class="param" id="USERID" value="' + paramList.id + '">';
                infoHTML += '<span class="param">Логин</span>';
                infoHTML += '<input class="param" onchange="updateUserData()" id="USERNAME" type="text" value="' + paramList.name + '">';
                infoHTML += '<span class="param">Электронная почта</span>';
                infoHTML += '<input class="param" onchange="updateUserData()" id="USERMAIL" type="text" value="' + paramList.email + '">';
                infoHTML += '<span class="param">Хэш пароля</span>';
                infoHTML += '<input disabled class="param" id="USERPASSHASH" value="' + paramList.hash + '">';
                infoHTML += '<span class="param">Комментарий</span>';
                infoHTML += '<input class="param" onchange="updateUserData()" id="USERNOTES" type="text" value="' + paramList.note + '">';
                elem.innerHTML=infoHTML;
//                      //Роли прилетают в виде массива, перебираем его и  
                //проставляем галочки в соответствии с ролями
                for (var key in paramList.userRoles)
                {
                    var elem = document.getElementById("ROLEID"+paramList.userRoles[key].id);
                    if(elem)
                    {
                        elem.checked=true;
                    }
                }
            }

//---------            
            function getAllUsers()
            {
                var response = $.ajax({url: "./userProcess/getAllUsers", async:false}).responseText;
                var contentTable = document.getElementById("contentTable");
                contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                var usersList = JSON.parse(response);
                for(var key in usersList)
                {
                    var d = document.createElement('tr');
                    d.innerHTML = '<td data-toggle="modal" data-target="#popup" onclick="showUserProperties('
                            + usersList[key].id + ')">' 
                            + usersList[key].name + '</td>';
                    contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                }
            }
//---------            
            function getUsersByRole(roleId)
            {
                var response = $.ajax({url: "./userProcess/getUsersByRole", data:{roleId:roleId}, async:false}).responseText;
                var contentTable = document.getElementById("contentTable");
                contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                var usersList = JSON.parse(response);
                for(var key in usersList)
                {
                    var d = document.createElement('tr');
                    d.innerHTML = '<td data-toggle="modal" data-target="#popup" onclick="showUserProperties('
                            + usersList[key].id + ')">' 
                            + usersList[key].name + '</td>';
                    contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                }
            }
//---------
            function getUserData(userId)
            {
                var response = $.ajax({url: "./userProcess/getUserData?userId="+userId,async:false}).responseText;
                return JSON.parse(response);
            }
//---------
            function getAllRoles()
            {
                var response = $.ajax({url: "./userProcess/getAllRoles",async:false}).responseText;
                return JSON.parse(response);
            }
//---------
            function updateUserData()
            {
                var userId = document.getElementById("USERID").value;
                var elem = window.event.target;
                var column = elem.id;
                var value = elem.value;
                var link = "./userProcess/updateUserData";
                $.ajax({
                  type: "POST",
                  url: link,
                  data: {userId:userId, column:column, value:value},
                  success: function(){
                            showAlertMessage("Данные пользователя обновлены");
                            getAllUsers();
                  }
                });
            }
//---------
            function updateUserRole(roleId)
            {
                var elem = window.event.target;
                var value = elem.checked;
                var userId = document.getElementById("USERID").value;
                var link = "./userProcess/updateUserRole";
                $.ajax({
                  type: "POST",
                  url: link,
                  data: {userId:userId, roleId:roleId, is:value},
                  success: function(){
                            showAlertMessage("Роль изменена");
                            getAllUsers();
                  }
                });
            }
//---------
            function getFileData(fileId)
            {
                var response = $.ajax({url: "./fileProcess/getFileData?fileId="+fileId,async:false}).responseText;
                return JSON.parse(response);
            }
            
    
//---------
            function getAllFiles()
            {
//                document.getElementById('properties').innerHTML='';
                var response = $.ajax({url: "./fileProcess/getAllFiles", async:false}).responseText;
                var filesList = JSON.parse(response);
                var contentTable = document.getElementById("contentTable");
                contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                filesList.forEach(function (item, i, arr)
                {
                    var d = document.createElement('tr');
                    d.innerHTML = '<td data-toggle="modal" data-target="#popup" onclick="showFileProperties(' + item.id + ')">' + item.name + '</td><td>' + item.ext + '</td>';
                    contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                });
            }
//---------
            function showFileProperties(fileId)
            {
                var paramList = getFileData(fileId);
                var propertiesDiv=document.getElementById('properties');
                propertiesDiv.innerHTML='';
                var elem=propertiesDiv.appendChild(document.createElement('div'));
                elem.id='paramsTable';
                for (var key in paramList)
                {
                    var owner = getUserData(paramList.owner);
                    var infoHTML = '';
                    infoHTML += '<img alt="дефолтная иконка" class="img-rounded" src="default_file.png"></br>';
                    infoHTML += '<label for="owner">Владелец</label>';
                    infoHTML += '<div id="owner" class="" data-toggle="modal" data-target="#properties" onclick="showUserProperties('+owner.id+')" class="btn-link">' + owner.name + '</div>';
                    infoHTML += '<label for="FILEID">Id</label>';
                    infoHTML += '<input disabled class="form-control" id="FILEID" value="' + paramList.id + '">';
                    infoHTML += '<label for="FILENAME">Имя файла</label>';
                    infoHTML += '<input class="form-control" onchange="updateFileData()" id="FILENAME" type="text" value="' + paramList.name + '">';
                    infoHTML += '<label for="FILEEXT">Расширение</label>';
                    infoHTML += '<input class="form-control" onchange="updateFileData()" id="FILEEXT" type="text" value="' + paramList.ext + '">';
                    infoHTML += '<label for="FILEDATA">Дата загрузки</label>';
                    infoHTML += '<input disabled class="form-control" id="FILEDATE" value="' + paramList.date + '">';
                    infoHTML += '<label for="FILEHASH">Хэш файла</label>';
                    infoHTML += '<input disabled class="form-control" id="FILEHASH" value="' + paramList.hash + '">';
                    elem.innerHTML = infoHTML;
                }
            }
//---------
            function updateFileData()
            {
                var fileId = document.getElementById("FILEID").value;
                var elem = window.event.target;
                var column = elem.id;
                var value = elem.value;
                var link = './fileProcess/updateFileData';
                $.ajax({
                  type: "POST",
                  url: link,
                  data: {fileId:fileId, column:column, value:value},
                  success: function(){
                            showAlertMessage("Данные файла обновлены");
                            getAllFiles();
                  }
              });
            }
            
//---------            
            function showAlertMessage(message)
            {
                $('#message-text').text(message);
                $('#message-container').fadeIn('slow');
                window.setTimeout(function () {
                    $("#message-container").slideUp(750, function () {
                    });
                }, 1000);
            }
//---------
            function popupShowUserProperties(userId)
            {
//                alert('надо сделать всплывающее окно с данными юзера');
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
                    <div class="dropdown">
                        <button class="btn btn-primary col-md-12 dropdown-toggle" type="button" data-toggle="dropdown">Пользователи
                            <span class="caret"></span></button>
                        <ul id="menu" class="dropdown-menu">
                            <li onclick="getAllUsers()"><a href="#">Все</a></li>
                        </ul>
                    </div>
                    <div class="btn btn-primary col-md-12" onclick="getAllFiles()">Файлы</div>
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
                    <div id="prop_erties" class="panel panel-default">
                    </div>
                </div>
            </div>
        </div>

    <div id="popup" class="modal fade" role="dialog">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
          </div>
          <div id="properties" class="modal-body">
            <p>Some text in the modal.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
          </div>
        </div>
      </div>
    </div>

    </body>


</html>
