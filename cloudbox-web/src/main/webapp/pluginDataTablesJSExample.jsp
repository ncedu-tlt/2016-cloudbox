<%-- 
    Document   : app
    Created on : Dec 18, 2015, 21:30:00 
    Author     : Victor
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Cloudbox</title>
        <link rel="icon" href="app/ico/cloudbox.ico" type="image/x-icon" />
        <!--<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>-->
        <link href="lib/bootstrap/css/bootstrap-paper.min.css" rel="stylesheet"/>
        <link href="lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
        <link href="https://cdn.datatables.net/1.10.10/css/jquery.dataTables.min.css" rel="stylesheet"/>       
        <link href="app/css/drive.css" rel="stylesheet">

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
        <script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.10/js/jquery.dataTables.min.js"></script>
        
        <script type="text/javascript" class="init">          
        $(document).ready(function() {
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
        } );
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
                </div>
            </nav>

            <div class="row">
                <div class="col-lg-2">
                    <a href="#" class="btn btn-danger col-lg-12">Загрузить файл <i class="fa fa-cloud-upload"></i></a>
                    <a href="#" class="btn btn-link col-lg-12">Мои файлы</a>
                    <a href="#" class="btn btn-link col-lg-12">Расшаренные мной</a>
                    <a href="#" class="btn btn-link col-lg-12">Доступные мне</a>
                    <a href="#" class="btn btn-link col-lg-12" >Корзина <i class="fa fa-trash"></i></a>
                </div>

                <div class="col-lg-8">
                    <div class="panel panel-default">
                        <table id="example" class="display" cellspacing="0" width="80%">
                            <thead>
                                <tr>
                                    <th>Название</th>
                                    <th>Расширение</th>
                                    <th>Дата создания</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Котики</td>
                                    <td>.jpg</td>
                                    <td>2015.11.11</td>
                                </tr>
                                <tr>
                                    <td>Владимирский централ</td>
                                    <td>.mp3</td>
                                    <td>2014.10.120</td>
                                </tr>
                                <tr>
                                    <td>ХХХ</td>
                                    <td>.doc</td>
                                    <td>2015.06.04</td>
                                </tr>
                                <tr>
                                    <td>Кровавая ночь</td>
                                    <td>.doc</td>
                                    <td>2011.01.15</td>
                                </tr>
                            </tbody>
                        </table>                      
                    </div>
                </div>

                <div class="col-lg-2">
                    <a href="#" class="btn btn-success col-lg-12">Скачать <i class="fa fa-cloud-download"></i></a>
                    <a href="#" class="col-lg-3 col-lg-offset-6"><i class="fa fa-share-alt fa-3x"></i></a>
                    <a href="#" class="col-lg-3"><i class="fa fa-trash fa-3x"></i></a>
                    <div class="form-group">
                        <label for="downloadLink" class="col-lg-12 control-label">Ссылка на файл</label>
                        <div class="col-lg-12"style="padding: 0px">
                            <input type="text" class="form-control" id="downloadLink" placeholder="downloadLink">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
