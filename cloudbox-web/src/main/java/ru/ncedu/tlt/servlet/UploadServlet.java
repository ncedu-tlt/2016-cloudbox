/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import java.io.IOException;
import java.io.File;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import ru.ncedu.tlt.hash.HashGenerator;

/**
 *
 * @author victori
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig/*(fileSizeThreshold=1024*1024*2, // 2MB - максимальный размер буфера для загрузки файла в оперативной памяти
                 maxFileSize=1024*1024*10000,   // 10000MB
                 maxRequestSize=1024*1024*50)   // 50MB*/

public class UploadServlet extends HttpServlet {
    private String DBFileName;
    private String DBFileExt;   
    private String DBFileHash;
    private Long DBFileDate;

    @EJB
    HashGenerator hg;
    //@EJB
    //FileController fcEjb;

    /**
     * Имя директории куда будут сохраняться загруженные файлы, относительно
     * директории web
     */
    private static final String SAVE_DIR = "uploadFiles";

    /**
     * Загрузка файла
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // получение полного пути до директории веб приложения
//        String appPath = request.getServletContext().getRealPath("");

        // создание пути до директории с загруженными файлами
        String savePath = /*appPath +*/ File.separator + SAVE_DIR;

        File fileSaveDir = new File(savePath);

        // создание директории если она ещё не создана //на linux не взлетело(( пока что папку uploadFiles нужно создавать вручную
//        fileSaveDir.mkdir();          
//        if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdir();
//        }
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            generateDBFieldsFromFile(fileName);
            //newFile = fcEjb.createFile(DBFileName, DBFileExt, DBFileDate, DBFileHash); //запись в БД, контроллер ещё не готов
            //fcEjb.addFile(newFile);
            part.write(fileSaveDir + File.separator + DBFileHash);
        }

        request.setAttribute("message", "Загрузка произведена успешно!");
        getServletContext().getRequestDispatcher("/uploadSuccessfulMessage.jsp").forward(
                request, response);
    }

    /**
     * Извлечение имени файла из заголовка HTTP content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    /**
     * Извлечение полей из файла для записи в БД
     */
    private void generateDBFieldsFromFile(String fileName) {
        int indexOfFileExt = fileName.lastIndexOf('.');     //индекс последнего вхождения '.' в имени файла
        DBFileExt = fileName.substring(indexOfFileExt, fileName.length());  //извлечение расширения из имени файла (для БД)
        DBFileName = fileName.substring(0, indexOfFileExt); //извлечение имени файла (для БД)
        DBFileDate = System.currentTimeMillis();
        String fileToHash = DBFileName + DBFileExt + DBFileDate.toString();
        DBFileHash = hg.getHash(fileToHash);
        fileToHash = null;
    }
}
