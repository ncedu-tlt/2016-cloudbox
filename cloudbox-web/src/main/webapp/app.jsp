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

        <link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>

        <script src="lib/jquery/jquery.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.min.js"></script>
    </head>
    <body>

        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">CloudBox</a>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="#">Диск<span class="sr-only">(current)</span></a></li>
                        <li class="active">

                            
                            <div class="col-lg-12" style="margin-top: 8px">
                                <select class="form-control" id="select">
                                    <option>Диск пользователя 1</option>
                                    <option>Диск пользователя 2</option>
                                    <option>Диск пользователя 3</option>
                                    <option>Диск пользователя 4</option>
                                    <option>Диск пользователя 5</option>
                                </select>
                                </div>

                        </li>
                        <li><a href="#">Администрирование</a></li>

                    </ul>
                    <!--                                        <form class="navbar-form navbar-left" role="search">
                                                                <div class="form-group">
                                                                    <input type="text" class="form-control" placeholder="Search">
                                                                </div>
                                                                <button type="submit" class="btn btn-default">Submit</button>
                                                            </form>-->
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">${userName} <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="#">Настройки</a></li>

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
                <a href="#" class="btn btn-danger col-lg-12">Загрузить файл</a>
                <a href="#" class="btn btn-link col-lg-12">Мои файлы</a>
                <a href="#" class="btn btn-link col-lg-12">Доступные мне</a>
                <a href="#" class="btn btn-link col-lg-12" >Корзина <span class="glyphicon glyphicon-trash"></span></a>
            </div>

            <div class="col-lg-8">
                
                <div class="row">
                    <table class="table table-striped table-hover ">
                        <thead>
                            <tr>
                                <th><input type="text" class="form-control" placeholder="Search..."> </th>
                                <th><select class="form-control" id="select">
                                    <option>Все</option>
                                    <option>TXT</option>
                                    <option>JPG</option>
                                    <option>DOC</option>
                                </select></th>
                                <th>Кнопка datepicker</th>
                            </tr>
                            <tr>
                                <th>Имя</th>
                                <th>Расширение</th>
                                <th>Дата</th>
                            </tr>
                        </thead>
                        <tbody>
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
                </div>
            </div>

            <div class="col-lg-2">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <a href="#" class="btn btn-success col-lg-12">Скачать <span class="glyphicon glyphicon-download"></span></a>
                        <p>Файл.тхт</p>
                        <a href="#" class="btn btn-link col-lg-2 col-lg-offset-8"><span class="glyphicon glyphicon-paperclip"></span></a>
                        <a href="#" class="btn btn-link col-lg-2"><span class="glyphicon glyphicon-trash"></span></a>
                        <div class="form-group">
                            <label for="downloadLink" class="col-lg-12 control-label">Ссылка на файл</label>
                            <div class="col-lg-12">
                                <input type="text" class="form-control" id="downloadLink" placeholder="downloadLink">
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>


    </body>
</html>
