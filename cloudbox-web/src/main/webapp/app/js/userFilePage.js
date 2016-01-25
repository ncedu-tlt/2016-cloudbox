SiteRootName = "http://192.168.1.21:8080/cloudbox-web";
function showAlertMessage(message, type)
{
    $('#alert').removeClass();
    $('#alert').addClass('alert alert-'+type);
    $('#message-text').text(message);
    $('#message-container').fadeIn('slow');
    window.setTimeout(function () {
        $("#message-container").slideUp(750, function () {
        });
    }, 1000);
}

function preparePage() {
    $(document).ready(function () {
        
        openOwnedFilesPanel();
        
        var toOwnedFilesList = document.getElementById("toOwnedFilesList");
        toOwnedFilesList.onclick = openOwnedFilesPanel;
        
        var toSharedFilesList = document.getElementById("toSharedFilesList");
        toSharedFilesList.onclick = openSharedFilesPanel;
        
        var toGarbageFilesList = document.getElementById("toGarbageFilesList");
        toGarbageFilesList.onclick = openGarbageFilesPanel;
        
        // table preparation
        var table = $('#mainTable').DataTable();

        $('#mainTable tbody').on('click', 'tr', function () {
            if ($(this).hasClass('selected')) {
                $(this).removeClass('selected');
            } else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
            }
        });

        $('#button').click(function () {
            table.row('.selected').remove().draw(false);
        });   
    });
};

//************  AJAX *************
function loadFileToServer(event) {
    if (event) {
        console.log("default off");
        event.stopPropagation();
        event.preventDefault();
    }
    ;

    var formData = new FormData(document.getElementById("loader"));

    // отослать
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange =
            function ()
            {
                console.log("ansver receved");
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                    if (xmlhttp.responseText === "OK") {
                        showAlertMessage("Файл успешно загружен!",'success');
                        updateFileTable("ownedFiles");
                    } else {
                        console.log("Server fail load fail" + xmlhttp.responseText);
                        showAlertMessage("Сервер не смог принять файл " + xmlhttp.responseText,'danger');
                    }

                } else if (xmlhttp.readyState === 4) {
                    showAlertMeggase("Ошибка загрузки файла ",'danger');
                }
                ;
            };

    xmlhttp.open("POST","uploadFiles", true);
    xmlhttp.send(formData);

}
;

function loadFileFromServer(event) {
    var url = "getFile";

    var file_arr = getMainTableSelectedRows();
    if (file_arr.length > 0) {                     
        url = url + "?fileId=" + file_arr[0];
        console.log(url);
        var ref = document.getElementById("hidden_link_get_file");
        ref.href = url;
        ref.click();
    };
};


function markFilesAsGarbage(event) {
    // ищем выделенные строки в таблице
    var arr = getMainTableSelectedRows();
    if (arr !== null) {
        var idFilesToDelete = JSON.stringify(arr);

        var formData = new FormData();

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange =
                function ()
                {
                    console.log("ansver receved");
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                        if (xmlhttp.responseText == "OK") {
                            showAlertMessage("Файлы удалены в корзину",'success');
                        } else {
                            console.log("Server fail" + xmlhttp.responseText);
                            showAlertMessage("Ошибка сервера при помещении в корзину",'danger');
                        }
                    } else if (xmlhttp.readyState === 4) {
                        showAlertMessage("Не удалось поместить в корзину",'danger');
                    };
                    updateFileTable("ownedFiles"); //Поместил эту строчку пока что сюда
                };
        xmlhttp.open("POST", "markFilesAsGarbage", true);
        //formData.append("listIdFiles", idFilesToDelete);
        xmlhttp.setRequestHeader("listIdFiles", idFilesToDelete);
        xmlhttp.send(formData);
    }
};

function restoreFilesFromGarbage(event) {
    var fileArr = getMainTableSelectedRows();

    if (fileArr.length > 0) {
        var idFilesToDelete = JSON.stringify(fileArr);

        var formData = new FormData();

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange =
                function ()
                {
                    console.log("ansver receved");
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                        if (xmlhttp.responseText === "OK") {
                            showAlertMessage("Файл восстановлен",'success');
                            updateFileTable("deletedFiles");
                        } else {
                            console.log(xmlhttp.responseText);
                            showAlertMessage("Ошибка сервера при восстановлении",'danger');
                        }
                    } else if (xmlhttp.readyState === 4) {
                            showAlertMessage("Ошибка загрузки файла",'danger');
                    }
                    ;
                };

        xmlhttp.open("POST", "FilesRestoreServlet", true);
        //formData.append("listIdFiles", idFilesToDelete);
        xmlhttp.setRequestHeader("listIdFiles", idFilesToDelete);
        xmlhttp.send(formData);
    } else {
        showAlertMessage("Нужно выбрать файл",'warning');
    }
};


function deleteFileFromServer(event) {
    // ищем выделенные строки в таблице
    var fileArr = getMainTableSelectedRows();
    
    if (fileArr.length > 0) {
        var idFilesToDelete = JSON.stringify(fileArr);

        var formData = new FormData();

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange =
                function ()
                {
                    console.log("ansver receved");
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                        if (xmlhttp.responseText === "OK") {
                            showAlertMessage("Файл успешно удален",'success');
                            updateFileTable("deletedFiles");
                        } else {
                            console.log("Server fail" + xmlhttp.responseText);
                            showAlertMessage("Ошибка сервера " + xmlhttp.responseText,'danger');
                        }
                        
                    } else if (xmlhttp.readyState === 4) {
                        showAlertMessage("Ошибка удаления файла",'danger');
                    }
                    ;
                };

        xmlhttp.open("POST", "deleteFiles", true);
        //formData.append("listIdFiles", idFilesToDelete);
        xmlhttp.setRequestHeader("listIdFiles", idFilesToDelete);
        xmlhttp.send(formData);
    }
}
;

function deleteLink(event) {
    // ищем выделенные строки в таблице
    var rows = document.getElementById('mainTable').children[1].children;
    var rowsLen = rows.length;
    var arr = new Array();
    var i = 0;

    for (i; i < rowsLen; i++) {
        console.log("num: " + i);
        if (rows[i].className === "selected")
            arr.push(rows[i].id);
    }
    ;
    if (arr.length > 0) {
        var idFilesToDelete = JSON.stringify(arr);

        var formData = new FormData();

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange =
                function ()
                {
                    console.log("ansver receved");
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                        if (xmlhttp.responseText === "OK") {
                            showAlertMessage("Ссылка удалена",'warning');
                            updateOwnedFilesTable();
                        } else {
                            console.log("Server fail load fail" + xmlhttp.responseText);
                            showAlertMessage("Ошибка сервера при удалении ссылки",'danger');
                        }
                    } else if (xmlhttp.readyState === 4) {
                        showAlertMessage("Ошибка удаления ссылки",'danger');
                    };
                };
                
        xmlhttp.open("POST", SiteRootName + "/linkServlet/delete", true);
        //formData.append("listIdFiles", idFilesToDelete);
        xmlhttp.setRequestHeader("listIdFiles", idFilesToDelete);
        xmlhttp.send(formData);
    } else {
        showAlertMessage("Нет выбранных файлов",'warning');
    }
};

function createLink(event) {
    // ищем выделенные строки в таблице
    var rows = document.getElementById('mainTable').children[1].children;
    var rowsLen = rows.length;
    var arr = new Array();
    var i = 0;

    for (i; i < rowsLen; i++) {
        console.log("num: " + i);
        if (rows[i].className === "selected")
            arr.push(rows[i].id);
    }
    ;
    if (arr.length > 0) {
        var idFilesToDelete = JSON.stringify(arr);

        var formData = new FormData();

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange =
                function ()
                {
                    console.log("ansver receved");
                    if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                        if (xmlhttp.responseText === "OK") {
                            alert("file in garbage");
                            updateOwnedFilesTable();
                        } else {
                            console.log("Server fail load fail" + xmlhttp.responseText);
                            alert("Server fail load fail");
                        }
                    } else if (xmlhttp.readyState === 4) {
                        alert("Fail while loading file");
                    };
                };
                
        xmlhttp.open("POST", SiteRootName + "/linkServlet/create", true);
        //formData.append("listIdFiles", idFilesToDelete);
        xmlhttp.setRequestHeader("listIdFiles", idFilesToDelete);
        xmlhttp.send(formData);
    } else {
        alert("No files is chosen");
    }
};


function getMainTableSelectedRows()
{
    var arr=[];
    $('.selected').each(function()
    {
        arr.push(this.id);
    });
    return arr;
}

/*
function getMainTableSelectedRows(){
    var rows = document.getElementById('mainTable').children[1].children;
    var rowsLen = rows.length;
    var arr = new Array();
    var i = 0;

    for (i; i < rowsLen; i++) {
        console.log("num: " + i);
        if (rows[i].classList.contains("selected"));
            arr.push(rows[i].id);
    };
    
    if (arr.length > 0 ) {return arr;}
    else{showAlertMessage("Необходимо выбрать файл",'warning');
          return null;};     
};
*/
//******************  Update table method *******************

function updateFileTable(type){
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange =
            function ()
            {
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
                {
                    console.log(xmlhttp.responseText);
                    filesList = JSON.parse(xmlhttp.responseText);
                    document.getElementById("mainTable").getElementsByTagName("tbody")[0].innerHTML = "";
                    for(var key in filesList)
                    {
                        console.log(filesList[key].name);
                    }
                    
                    filesList.forEach(function (item, i, arr) {
                        var d = document.createElement('tr');
                        d.id = item.id;
                        d.innerHTML = "<td>" + item.name + "</td>" + "<td>" + item.ext + "</td>" +"<td>" + item.date + "</td>";
                        document.getElementById("mainTable").getElementsByTagName("tbody")[0].appendChild(d);
                    });
                }
                else{
//                    console.log(xmlhttp.readyState + "    "+ xmlhttp.responseText);                    
                }
            };
    xmlhttp.open("GET", "getFileList/" + type, true); //?
    xmlhttp.send();
};

//******************  HTML dinamic logic*********************
function openOwnedFilesPanel(event){
    console.log("openOwnedFilesPanel" + " was invoked /n");
    
    leftPanelMakeOneButtonActive("toOwnedFilesList");
    /*
    var link = document.querySelector('link[rel="import"]');
    var doc = link.import;
    console.log(doc.querySelector('.doc'));
    document.getElementById("rightButtonPanel").innerHTML = doc.querySelector('.doc').innerHTML;
    */
    document.getElementById("rightButtonPanel").innerHTML = document.getElementById("ownFileRightPanel").innerHTML;
    
    var loaderFile = document.getElementById("loader");
    loaderFile.onsubmit = loadFileToServer;
        
    var markGarbage = document.getElementById("putToGarbageButton");
    markGarbage.onclick = markFilesAsGarbage;
    
    var getFileButton = document.getElementById("getFileButton");
    getFileButton.onclick = loadFileFromServer;
    
    updateFileTable("ownedFiles");
    
};

function openSharedFilesPanel(event){ 
    console.log("openSharedFilesPanel" + " was invoked /n");
    leftPanelMakeOneButtonActive("toSharedFilesList");
    
    document.getElementById("rightButtonPanel").innerHTML = document.getElementById("sharedFileRightPanel").innerHTML; 
    
    var delLinkButton = document.getElementById("deleteLinkButton");
    delLinkButton.onclick = deleteLink;
    
    var getFileButton = document.getElementById("getFileButton");
    getFileButton.onclick = loadFileFromServer;
    
    updateFileTable("sharedFiles");
};

function openGarbageFilesPanel(event){  
    leftPanelMakeOneButtonActive("toGarbageFilesList");
    console.log("openGarbageFilesPanel" + " was invoked /n");
    document.getElementById("rightButtonPanel").innerHTML = document.getElementById("garbageFilesRightPanel").innerHTML;
    
    var deleteFileButton = document.getElementById("deleteButton");
    deleteFileButton.onclick = deleteFileFromServer;
    
    var restoreButton = document.getElementById("restoreFileButton");
    restoreButton.onclick = restoreFilesFromGarbage;
    
    updateFileTable("deletedFiles");
};

function leftPanelMakeOneButtonActive(button){
    var buttons_names = new Array("toOwnedFilesList","toSharedFilesList","toGarbageFilesList");
    buttons_names.forEach(function (item, i, arr){
        var curr_button = document.getElementById(item);
        if(item===button){
            console.log("now this button is active: " + item + "c");
            curr_button.setAttribute("class","btn btn-info col-lg-12");
        }
        else{
            console.log("now this button is passive: " + item + "c");
            curr_button.setAttribute("class","btn btn-link col-lg-12");
        }
    });
};