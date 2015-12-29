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
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;


/**
 *
 * @author victori
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig /*(location="/tmp")(fileSizeThreshold=1024*1024*2, // 2MB - максимальный размер буфера для загрузки файла в оперативной памяти
                 maxFileSize=1024*1024*10000,   // 10000MB
                 maxRequestSize=1024*1024*50)   // 50MB*/

public class UploadServlet extends HttpServlet {

    @EJB
    EntityFileController entityFileController;
    private static final String SAVE_DIR = "uploadFiles"; //название папки куда будут сохраняться файлы

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        if (request.getParameter("userId") != null) {   //проверка на логин сессии 

//          String appPath = request.getServletContext().getRealPath("");   //можно задать путь до произвольной папки системы
            String savePath = /*appPath +*/ File.separator + SAVE_DIR;
            File fileSaveDir = new File(savePath);

//          создание директории если она ещё не создана //не работает чёт //TODO вероятно перейти на java.util.io или .nio
//          fileSaveDir.mkdir();          
//          if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdir();
//          }

            for (Part part : request.getParts()) {              //TODO перенести в бин работы с хардом
                String fileName = extractFileName(part);
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                EntityFile eFile = entityFileController.createEntityFile(fileName, userId);
                part.write(fileSaveDir + File.separator + eFile.getHash());
            }

            request.setAttribute("message", "Загрузка произведена успешно!");
            getServletContext().getRequestDispatcher("/uploadSuccessfulMessage.jsp").forward(
                    request, response);
        } else {
            request.setAttribute("message", "Вы не в сессии!");
            getServletContext().getRequestDispatcher("/uploadSuccessfulMessage.jsp").forward(
                    request, response);
        }
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
}