
function preparePage (){   
    $(document).ready(function() {
            // loader preparation
            var loaderFile = document.getElementById("loader");
            loaderFile.onsubmit = loadFileToServer;
            
            var deleteFileButton = document.getElementById("deleteButton");
            deleteFileButton.onclick = deleteFileFromServer;
            
            var loaderFile = document.getElementById("getFileButton");
                loaderFile.onclick = loadFileFromServer;
            
            // table preparation
            var table = $('#example').DataTable();
 
            $('#example tbody').on( 'click', 'tr', function () {
                if ( $(this).hasClass('selected') ) {
                    $(this).removeClass('selected');
                }
                else {
                    table.$('tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                }
            } );
 
            $('#button').click( function () {
                table.row('.selected').remove().draw( false );
            } );           
            
            updateUserFileTable();
        });
};

function loadFileToServer(event){
    if(event){console.log("default off");
                event.stopPropagation();
                event.preventDefault();};
    
    var formData = new FormData(document.getElementById("loader"));
    
    // отослать
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = 
                function()
                {
                    console.log("ansver receved");
                    if(xmlhttp.readyState === 4 && xmlhttp.status === 200){
                            if(xmlhttp.responseText === "OK"){
                                alert("file loaded");
                                updateUserFileTable();
                            }
                            else {
                                console.log("Server fail load fail"+ xmlhttp.responseText);
                                alert("Server fail load fail"+ xmlhttp.responseText);
                            } 
                            
                    }
                    else if(xmlhttp.readyState === 4){
                        alert("Fail while loading file");
                    };
                };
                
    xmlhttp.open("POST", "uploadFiles",true);
    xmlhttp.send(formData);
    
};

function loadFileFromServer(event){
    var url = "getFile";
    
    var rows = document.getElementById('example').children[1].children;
    var rowsLen = rows.length;
	var arr = new Array();
	var i = 0;

	for(i; i<rowsLen; i++){
		console.log("num: " + i);
		if(rows[i].className === "selected") arr.push(rows[i].id);
	};
        if(arr.length > 0){
            url = url + "?fileId=" + arr[0];
            console.log(url);
            var ref = document.getElementById("hidden_link_get_file");
            ref.href=url;
            ref.click();
        }
        else{
            alert("файл не выбран");
        }
};

function deleteFileFromServer(event){
        // ищем выделенные строки в таблице
    	var rows = document.getElementById('example').children[1].children;
	var rowsLen = rows.length;
	var arr = new Array();
	var i = 0;

	for(i; i<rowsLen; i++){
		console.log("num: " + i);
		if(rows[i].className === "selected") arr.push(rows[i].id);
	};
        if(arr.length > 0){
            var idFilesToDelete = JSON.stringify(arr);

            var formData = new FormData();

            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = 
                    function()
                    {
                        console.log("ansver receved");
                        if(xmlhttp.readyState === 4 && xmlhttp.status === 200){
                                if(xmlhttp.responseText === "OK"){
                                    alert("file deleted");
                                    updateUserFileTable();
                                }
                                else {
                                    console.log("Server fail load fail"+ xmlhttp.responseText);
                                    alert("Server fail load fail"+ xmlhttp.responseText);
                                } 

                        }
                        else if(xmlhttp.readyState === 4){
                            alert("Fail while loading file");
                        };
                    };
                    
            xmlhttp.open("POST", "deleteFiles",true);       
            //formData.append("listIdFiles", idFilesToDelete);
            xmlhttp.setRequestHeader("listIdFiles",idFilesToDelete);            
            xmlhttp.send(formData);    
        }
        else{
            alert("No files is chosen");
        }
};

function updateUserFileTable() {
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = 
                function()
                {
                    if(xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        console.log(xmlhttp.responseText);
                        filesList = JSON.parse(xmlhttp.responseText);
                        document.getElementById("example").getElementsByTagName("tbody")[0].innerHTML = "";
                        filesList.forEach(function(item, i, arr){
                            var d = document.createElement('tr');
                            d.id = item.id;
                            d.innerHTML = "<td>" + item.name + "</td>" + "<td>" + item.ext + "</td>" + + "<td>" + item.date + "</td>";
                            document.getElementById("example").getElementsByTagName("tbody")[0].appendChild(d);
                        });
                    }
                };
                xmlhttp.open("GET","http://localhost:8080/cloudbox-web/fileProcess/getFilesList",true);
                xmlhttp.send();
            };