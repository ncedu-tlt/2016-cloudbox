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
            
//--------- Формирует список ролей и рисует пункты выпадающего меню.
            var rolesList = getAllRoles();

            //проверка отсутствия наличия в хранилище
            
            $(document).ready(function(){
                showAllUsers('all');
                showAllFiles('all');
                for(var key in rolesList)
                {
                    $('#menu').append('<li onclick="showAllUsers('+rolesList[key].id+')"><a>'+rolesList[key].name+'</a></li>');
                };
            });
//--------- Получает список UserRoles из базы
            function getAllRoles()
            {
                var response = $.ajax({url: "./userProcess/getAllRoles",async:false}).responseText;
                return JSON.parse(response);
            }
            
//--------- Получает объект User из базы.
            function getUserData(userId)
            {
                var response = $.ajax({url: "./userProcess/getUserData?userId="+userId,async:false}).responseText;
                return JSON.parse(response);
            }
            
//--------- Получает объект File из базы.
            function getFileData(fileId)
            {
                var response = $.ajax({url: "./fileProcess/getFileData?fileId="+fileId,async:false}).responseText;
                return JSON.parse(response);
            }

//--------- Получает список User выбранной роли, 
//          если roleId = "all" тогда всех
            function getAllUsers(roleId)
            {
                var url = "./userProcess/getAllUsers";
                if(roleId !== "all")
                {
                    url = "./userProcess/getUsersByRole?roleId="+roleId; 
                }
                var usersList = JSON.parse(
                        $.ajax({
                            url: url, 
                            async:false
                        }).responseText);
                return usersList;
            }

//--------- Рисует проперти аккаунта           
            function showUserProperties(userId)
            {
                var paramList = getUserData(userId);
                var rolesString='';
                $('#properties').empty();
                $('#popupTitle').html(paramList.name);
                $('#properties').append('<div id="paramsTable" class="form-group form-group-sm"></div>');
                var infoHTML = '<img alt="фотка персонажа" class="img-rounded" src="' + paramList.picPath + '"></br>';
                infoHTML += '<span class="label label-default">UID</span>';
                infoHTML += '<input readonly class="form-control" id="USERID" value="' + paramList.id + '">';
                infoHTML += '<span class="label label-default">Логин</span>';
                infoHTML += '<input class="form-control" onchange="updateUserData()" id="USERNAME" type="text" value="' + paramList.name + '">';
                infoHTML += '<span class="label label-default">Электронная почта</span>';
                infoHTML += '<input class="form-control" onchange="updateUserData()" id="USERMAIL" type="text" value="' + paramList.email + '">';
                infoHTML += '<span class="label label-default">Хэш пароля</span>';
                infoHTML += '<input readonly class="form-control" id="USERPASSHASH" value="' + paramList.hash + '">';
                infoHTML += '<span class="label label-default">Комментарий</span>';
                infoHTML += '<input class="form-control" onchange="updateUserData()" id="USERNOTES" type="text" value="' + paramList.note + '">';
                $('#paramsTable').html(infoHTML);
                //Роли прилетают в виде массива, перебираем его и  
                //проставляем галочки в соответствии с ролями
                for(var key in rolesList)
                {
                    rolesString = '<input type="checkbox" id="'
                    +rolesList[key].id
                    +'">'
                    +rolesList[key].name
                    + '<br>';
                    $('#paramsTable').append(rolesString);
                }

                for (var key in paramList.userRoles)
                {
                    var id=paramList.userRoles[key].id;
                    $('#'+id).prop("checked", true);
                }
                $(':checkbox').change(function ()
                {
                    var userId = $("#USERID").val();
                    var roleId = $(this).prop('id');
                    var value = $(this).prop('checked');
                    var link = "./userProcess/updateUserRole";
                    $.ajax({
                      type: "POST",
                      url: link,
                      data: {userId:userId, roleId:roleId, is:value},
                      success: function(){
                                showAlertMessage("Роль изменена");
                                showAllUsers(roleId);
                      }
                    });
                });
                
            }

//--------- Рисует список юзеров соответствующей роли, либо 'all'
            function showAllUsers(roleId)
            {
                $('#rolesButtons').children().removeClass('btn-primary');
                $('#rolesButtons').children().addClass('btn-success');
                $('#rolesButtons').children('#id'+roleId).removeClass('btn-success');
                $('#rolesButtons').children('#id'+roleId).addClass('btn-primary');
                var usersList = getAllUsers(roleId);
                $('#usersTable').empty();
                for(var key in usersList)
                {
                    d = '<tr class="row">' 
                            + '<td class=""><img class="img-thumbnail" src="app/img/userpic.png" width="32"></td>'
                            + '<td class="" data-toggle="modal" data-target="#popup" onclick="showUserProperties('
                            + usersList[key].id + ')">'+usersList[key].name + '</td>'
                            + '<td class="" onclick="showAllFiles('+usersList[key].id+')" align="right">'
                            +'<div class="btn glyphicon glyphicon-filter"></div></td>'
                    +"</tr>";
                    $('#usersTable').append(d);
                }
            }
            
//--------- Рисует проперти файла
            function showFileProperties(fileId)
            {
                var paramList = getFileData(fileId);
                $('#properties').empty();
                $('#popupTitle').html(paramList.name);
                $('#properties').append('<div id="paramsTable"></div>');
                var owner = getUserData(paramList.owner);
                var infoHTML = '<img alt="Иконка" class="img-rounded" src="app/img/filetypes/png/'+paramList.ext+'.png"></br>';
                infoHTML += '<span class="label label-info"> Владелец </span>';
                infoHTML += '<div id="owner" class="label label-warning" data-toggle="modal" data-target="#properties" onclick="showUserProperties('+owner.id+')">' + owner.name + '</div>';
                infoHTML += '<span class="label label-info"> Id </span>';
                infoHTML += '<input disabled class="form-control" id="FILEID" value="' + paramList.id + '">';
                infoHTML += '<span class="label label-info"> Имя файла </span>';
                infoHTML += '<input class="form-control" onchange="updateFileData()" id="FILENAME" type="text" value="' + paramList.name + '">';
                infoHTML += '<span class="label label-info"> Расширение </span>';
                infoHTML += '<input class="form-control" onchange="updateFileData()" id="FILEEXT" type="text" value="' + paramList.ext + '">';
                infoHTML += '<span class="label label-info"> Дата загрузки </span>';
                infoHTML += '<input readonly class="form-control" id="FILEDATE" value="' + paramList.date + '">';
                infoHTML += '<span class="label label-info"> Хэш файла </span>';
                infoHTML += '<input readonly class="form-control" id="FILEHASH" value="' + paramList.hash + '">';
                $('#paramsTable').html(infoHTML);
            }

//---------
            function updateUserData()
            {
                var userId = $('#USERID').val();
                var column = window.event.target.id;
                var value = window.event.target.value;
                var link = "./userProcess/updateUserData";
                $.ajax({
                  type: "POST",
                  url: link,
                  data: {userId:userId, column:column, value:value},
                  success: function(){
                            showAlertMessage("Данные пользователя обновлены");
                            showAllUsers('all');
                  }
                });
            }
//---------
            function getAllFiles(userId)
            {
                var url = "./fileProcess/getAllFiles";
                if(userId !== 'all')
                {
                    url = "./fileProcess/getUserFiles?userId="+userId;
                }
                var filesList = JSON.parse(
                        $.ajax({
                            url: url, 
                            async:false
                        }).responseText);
                return filesList;
            }
//---------            
            function showAllFiles(userId)
            {
                var filesList = getAllFiles(userId);
                $('#filesTable').empty();
                for(var key in filesList)
                {
                    var c='class=""';
                    var exist = $.ajax({url: "fileProcess/checkFile?fileId="+filesList[key].id, async:false}).responseText;
                    if(exist==='false')
                    {
                        c = 'class="warning"';
                    }
                    d = '<tr '+c+' data-toggle="modal" data-target="#popup" onclick="showFileProperties('
                            + filesList[key].id + ')">'
                            + '<td><img alt="icon" src="app/img/filetypes/png/'+filesList[key].ext + '.png" style="width:32px;"></td>'
                            + '<td>'+filesList[key].name + '</td>'
                            + '<td>'+filesList[key].ext + '</td>'
                            + '<td>'+filesList[key].date + '</td>';
                    $('#filesTable').append(d);
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
        </script>
    </head>
    
    <div class="container">
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
                            <li ><a href="drive.jsp">Мой диск<span class="sr-only">(current)</span></a></li>

                            <li class="active"><a href="adminpage_1.jsp">Администрирование</a></li>

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
                <div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Пользователи</div>
                        <div id="rolesButtons" class="btn-group btn-group-justified">
                            <a id="idall" class="btn btn-success" onclick="showAllUsers('all')"> Все </a>
                            <a id="id1" class="btn btn-success" onclick="showAllUsers(1)"> Администратор </a>
                            <a id="id2" class="btn btn-success" onclick="showAllUsers(2)"> Модератор </a>
                            <a id="id3" class="btn btn-success" onclick="showAllUsers(3)"> Пользователь </a>
                        </div>
                        <table id="usersTable" class="table table-hover " cellspacing="0" width="100%">
                        </table>                      
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Файлы
                        </div>
                        <div class="">
                            <div class="btn-group btn-group-justified">
                                <a class="btn btn-success" onclick="showAllFiles('all')"> Все </a>
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
