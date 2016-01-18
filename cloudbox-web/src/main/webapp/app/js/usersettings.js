 $( document ).ready(function() {           
//---------
//Так как роли у нас фиксированы для всех функций, то сразу формируем переменную, содержащую список ролей.
            var rolesList = getAllRoles();
//---------            
           var showUserProperties =   function ()
            {
                var paramList = getUserLoggedData();
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
                var infoHTML = '<img alt="фотка персонажа" class="foto" src="' + paramList.picPath + '"></br>';
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
                    d.innerHTML = "<td onclick=\"showUserProperties("
                            + usersList[key].id + ")\">" 
                            + usersList[key].name + "</td>";
                    contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                }
            }
//---------
            function getUserData(userId)
            {
                var response = $.ajax({url: "./userProcess/getUserData?userId="+userId,async:false}).responseText;
                return JSON.parse(response);
            }
            
             function getUserLoggedData()
            {
                var response = $.ajax({url: "./userProcess/getUserLoggedData",async:false}).responseText;
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
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function ()
                {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        var filesList = JSON.parse(xmlhttp.responseText);
                        var contentTable = document.getElementById("contentTable");
                        contentTable.getElementsByTagName("tbody")[0].innerHTML = "";
                        filesList.forEach(function (item, i, arr)
                        {
                            var d = document.createElement('tr');
                            d.innerHTML = '<td onclick="showFileProperties(' + item.id + ')">' + item.name + '</td><td>' + item.ext + '</td>';
                            contentTable.getElementsByTagName("tbody")[0].appendChild(d);
                        });
                    }
                };
                xmlhttp.open("GET", "./fileProcess/getFilesList", false);
                xmlhttp.send();
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
                    infoHTML += '<img alt="дефолтная иконка" class="foto" src="default_file.png"></br>';
                    infoHTML += '<span class="param">Владелец</span>';
                    infoHTML += '<input type=button class="btn-link" onclick="showUserProperties(' + owner.id + ')" value="' + owner.name + '">';
                    infoHTML += '<span class="param">Id</span>';
                    infoHTML += '<input disabled class="param" id="FILEID" value="' + paramList.id + '">';
                    infoHTML += '<span class="param">Имя файла</span>';
                    infoHTML += '<input class="param" onchange="updateFileData()" id="FILEUSERID" type="text" value="' + paramList.name + '">';
                    infoHTML += '<span class="param">Расширение</span>';
                    infoHTML += '<input class="param" onchange="updateFileData()" id="FILEEXT" type="text" value="' + paramList.ext + '">';
                    infoHTML += '<span class="param">Дата загрузки</span>';
                    infoHTML += '<input class="param" onchange="updateFileData()" id="FILEDATE" type="text" value="' + paramList.date + '">';
                    infoHTML += '<span class="param">Хэш файла</span>';
                    infoHTML += '<input disabled class="param" id="FILEHASH" value="' + paramList.hash + '">';
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
                var link = "./fileProcess/updateFileData?fileId=" + fileId
                        + "&column=" + column
                        + "&value=" + value;
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function () {
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        showAlertMessage("Данные о файле обновлены");
                        getAllFiles();
                    }
                };
                xmlhttp.open("GET", link, true);
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

showUserProperties();

 });