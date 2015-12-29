 function loadToServer(event){
    if(event){console.log("default off");
                event.stopPropagation();
                event.preventDefault();};
    
    var formData = new FormData(document.getElementById("loader"));
    
    // отослать
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = 
                function()
                {
                    if(xhr.readyState === 4 && xhr.status === 200)
                    {
                        console.log(xhr.responseText);
                    }
                };
                
    xhr.open("POST", "upload");
    xhr.send(formData);
    
};
    


document.addEventListener("DOMContentLoaded", function(){    
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
            //******************************************
            
            
                //myLoader.addEventListener("submit", loadToServer,false);  // TODO WTF don't work
                var loaderFile = document.getElementById("getFileButton");
                loaderFile.onclick = loadFileFromServer;
                
    });
    
    
function FALSEloadFile(event){
    var url = "TestFileServlet";
    url = url + "?fileId=" + 8;
    
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
                    if(xmlhttp.readyState === 4 && xmlhttp.status === 200)
                    {
                        console.log(xmlhttp.responseText);
                    }
                };
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
};
 
function loadFileFromServer(event){
    var url = "TestFileServlet";
    
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
            var ref = document.getElementById("test_link");
            ref.href=url;
            ref.click();
        }
        else{
            alert("файл не выбран");
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
