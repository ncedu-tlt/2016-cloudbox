            
//--------- Формирует список ролей и рисует пункты выпадающего меню.
            var rolesList = getAllRoles();

            $(document).ready(function(){
                
                $('#rolesButtons').append('<a data-roleId="all"> Все </a>');
                for(var key in rolesList)
                {
                    $('#rolesButtons').append('<a data-roleId="'+rolesList[key].id+'">'+rolesList[key].name+'</a>');
                };                
                $('#rolesButtons>a').addClass('btn').click(function(){
                    $('#rolesButtons>a').removeClass('btn-primary');
                    $(this).toggleClass('btn-primary');
                    var roleId=$(this).attr('data-roleid');
                    getAllUsers(roleId);
                    
                });
                getAllUsers('all');
                getAllFiles('all');
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
                showUsersList(usersList);
            }

//--------- Получает список User имеющих доступ к файлу, 
            function getFileUsers(fileId)
            {
                var url = "./userProcess/getFileUsers?fileId="+fileId; 
                var usersList = JSON.parse(
                        $.ajax({
                            url: url, 
                            async:false
                        }).responseText);
                $('#filesTable>tbody>tr').removeClass('info');       
                $('#fileId'+fileId).addClass('info');
                showUsersList(usersList);
            }
//---------
            function getAllFiles(userId)
            {
                var filesList;
                var url = "./fileProcess/getAllFiles";
                if(userId !== 'all')
                {
                    $('#filesPanelHead').text('Файлы пользователя - '+userId);
                    url = "./fileProcess/getUserFiles?userId="+userId;
                    var ownFilesList = JSON.parse(
                            $.ajax({
                                url: url, 
                                async:false
                            }).responseText);
                    url = "./fileProcess/getSharedUserFiles?userId="+userId;
                    var sharedFilesList = JSON.parse(
                            $.ajax({
                                url: url, 
                                async:false
                            }).responseText);
                            
                    filesList = ownFilesList.concat(sharedFilesList);
                }
                else
                {
                    $('#filesPanelHead').text('Все файлы');
                    filesList = JSON.parse(
                            $.ajax({
                                url: url, 
                                async:false
                            }).responseText);
                }
                showFilesList(filesList);
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
                                getAllUsers(roleId);
                      }
                    });
                });
                
            }

//--------- Рисует список юзеров соответствующей роли, либо 'all'
            function showUsersList(usersList)
            {
                $('#usersTable').empty();
                for(var key in usersList)
                {
                    var d = '<tr class="row">' 
                            + '<td class=""><img class="img-thumbnail" src="app/img/userpic.png" width="32"></td>'
                            + '<td class="" data-toggle="modal" data-target="#popup" onclick="showUserProperties('
                            + usersList[key].id + ')">'+usersList[key].name + '</td>'
                            + '<td class="" align="right">'
                            +'<div class="btn glyphicon glyphicon-filter" onclick="getAllFiles('+usersList[key].id+')"></div></td>'
                    +"</tr>";
                    $('#usersTable').append(d);
                }
            }
//---------            
            function showFilesList(filesList)
            {
                $('#filesTable').empty();
                for(var key in filesList)
                {
                    var file = filesList[key];
                    var current = 'fileId'+file.id;
                    $('#filesTable').append('<tr id="'+current+'">');
                    var exist = $.ajax({url: "fileProcess/checkFile?fileId="+file.id, async:false}).responseText;
                    if(exist==='false')
                    {
                        $('#'+current).addClass('warning');
                    }
                    var d = '<td><img alt="icon" src="app/img/filetypes/png/'+file.ext + '.png" style="width:32px;"></td>'
                            + '<td  data-toggle="modal" data-target="#popup" onclick="showFileProperties('+file.id+')">'+file.name +'.'+ file.ext + '</td>'
                            + '<td>'+file.date + '</td>'
                            + '<td class="" onclick="getFileUsers('+filesList[key].id+')" align="right">'
                            +'<div class="btn glyphicon glyphicon-filter"></div></td>';
                    $('#'+current).append(d);
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
                            showUsersList('all');
                  }
                });
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
