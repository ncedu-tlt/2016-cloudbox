<%-- 
    Document   : app
    Created on : Dec 11, 2015, 2:07:04 AM
    Author     : Andrew
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
        
        <link href="app/css/drive.css" rel="stylesheet">


        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
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
                            <li class="active"><a href="#">Мой диск<span class="sr-only">(current)</span></a></li>
                            <li class="active">


                                <div class="col-lg-12" style="margin-top: 8px">
                                    <select class="form-control" id="select">

                                        <option> Мой диск</option>
                                        <option>Диск пользователя 1</option>
                                        <option>Диск пользователя 2</option>
                                        <option>Диск пользователя 3</option>
                                        <option>Диск пользователя 4</option>
                                        <option>Диск пользователя 5</option>
                                    </select>
                                </div>

                            </li>
                            <li><a href="adminpage.jsp">Администрирование</a></li>

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
                    <a href="#" class="btn btn-danger col-lg-12">Загрузить файл <i class="fa fa-cloud-upload"></i></a>
                    <a href="#" class="btn btn-link col-lg-12">Мои файлы</a>
                    <a href="#" class="btn btn-link col-lg-12">Доступные мне</a>
                    <a href="#" class="btn btn-link col-lg-12" >Корзина <i class="fa fa-trash"></i></a>
                </div>



                <div class="col-lg-7">
                    <div class="panel panel-default">
                        <!--<div class="panel-body">-->


                        <div >
                            
                        <table class="table table-striped table-hover " >
                            <thead>
                                <tr>
                                    <th><input type="text" class="form-control" placeholder="Поиск..."> </th>
                                    <th><select class="form-control" id="select">
                                            <option>Все</option>
                                            <option>TXT</option>
                                            <option>JPG</option>
                                            <option>DOC</option>
                                        </select></th>
                                    <th >
                                        <div class="input-group date" data-provide="datepicker">
                                            <input type="text" class="form-control" value="31.12.15">
                                            <div class="input-group-addon">
                                                <span class="glyphicon glyphicon-th"></span>
                                            </div>
                                        </div>  


                                    </th>
                                </tr>
                                <tr>
                                    <th>Имя</th>
                                    <th>Расширение</th>
                                    <th>Дата</th>
                                </tr>
                            </thead>
                            <tbody >
                                <tr>
                                    <td><a href="./filetest">Война и мир</a></td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr >
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                                <tr>
                                    <td>Война и мир</td>
                                    <td>doc</td>
                                    <td>31.12.15</td>
                                </tr>
                            </tbody>
                        </table>   
                        <!--</div>--> 
</div>
                    </div>



                </div>

                <div class="col-lg-3">
                    <a href="#" class="btn btn-success col-lg-12">Скачать <i class="fa fa-cloud-download"></i></a>
                            <p>Файл.тхт</p>
                            <a href="#" class="col-lg-3 col-lg-offset-6"><i class="fa fa-share-alt fa-3x"></i></a>
                            <a href="#" class="col-lg-3"><i class="fa fa-trash fa-3x"></i></a>
                            <div class="form-group">
                                <label for="downloadLink" class="col-lg-12 control-label">Ссылка на файл</label>
                                <div class="col-lg-12"style="padding: 0px">
                                    <input type="text" class="form-control" id="downloadLink" placeholder="downloadLink">
                                </div>
                            </div>
                    
<!--                    <div class="panel panel-default">
                        <div class="panel-body" style="padding: 7px">
                            <a href="#" class="btn btn-success col-lg-12">Скачать <i class="fa fa-cloud-download"></i></a>
                            <p>Файл.тхт</p>
                            <a href="#" class="col-lg-3 col-lg-offset-6"><i class="fa fa-share-alt fa-3x"></i></a>
                            <a href="#" class="col-lg-3"><i class="fa fa-trash fa-3x"></i></a>
                            <div class="form-group">
                                <label for="downloadLink" class="col-lg-12 control-label">Ссылка на файл</label>
                                <div class="col-lg-12"style="padding: 0px">
                                    <input type="text" class="form-control" id="downloadLink" placeholder="downloadLink">
                                </div>
                            </div>
                        </div>
                    </div>-->

                </div>

            </div>

        </div>


    </body>
</html>
