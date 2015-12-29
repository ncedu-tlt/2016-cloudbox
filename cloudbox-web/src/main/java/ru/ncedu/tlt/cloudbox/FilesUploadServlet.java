/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import ru.ncedu.tlt.entity.File;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "FilesUploadServlet", urlPatterns = {"/uploadFiles"})
@MultipartConfig
public class FilesUploadServlet extends HttpServlet {

    
    @EJB 
    FileWorkerBean fileWorker;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        //int userID = Integer.getInteger((String)request.getSession().getAttribute("userName")); 
        // TODO получить нормальный ID проверить можно ли объект сразу привести к int
        
        Part filePart = request.getPart("file");
        String fullFileName = filePart.getSubmittedFileName();
        InputStream fileStream = filePart.getInputStream();
        PrintWriter resp = response.getWriter();
        
        String[] fileSplittedName = fullFileName.split("[.]");
        
        String fileName = "";
        String fileExt = null;
        int len = fileSplittedName.length;
        if(len > 1) {    
            fileExt = fileSplittedName[len-1];
            len-=1;
        };
        fileName = fileSplittedName[0];
        int i = 1;
        while(i<len){
            fileName = fileName.concat(".");
            fileName = fileName.concat(fileSplittedName[i]);
            i++;
        }
        
        File file = new File();
        file.setName(fileName);
        file.setExt(fileExt);
        
        int userID = 5;  // TODO сменить на реальный из сессии

        String result = fileWorker.storeFile(file, userID ,fileStream);
        
        resp.write(result);
    };     
}
