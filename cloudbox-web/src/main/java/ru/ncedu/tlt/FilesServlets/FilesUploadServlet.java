/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.FilesServlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.utils.DiskUtils;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "FilesUploadServlet", urlPatterns = {"/uploadFiles"})
@MultipartConfig
public class FilesUploadServlet extends HttpServlet {

    @EJB
    EntityFileController enityFileController;
    
    @EJB
    DiskUtils diskUtils;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println(request);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println(request);
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        
        Part filePart = request.getPart("file");
        String fullFileName = filePart.getSubmittedFileName();
        InputStream fileStream = filePart.getInputStream();
        
        System.out.println("Файл - :"+filePart);
        
        PrintWriter resp = response.getWriter();
        EntityFile entityFile = new EntityFile(); 
        
        int indexOfFileExt = fullFileName.lastIndexOf('.');
        entityFile.setName(fullFileName.substring(0, indexOfFileExt));
        entityFile.setExt(fullFileName.substring(indexOfFileExt+1, fullFileName.length()));
        entityFile.setDate(Service.getCurrentTimeStamp());
        entityFile.setHash(Service.getRandomUUID());
        entityFile.setOwner(userId);        
        
        try {                            
            diskUtils.storeFile(fileStream, entityFile.getHash());    // TODO добавить откат если сохранение не удалось
            enityFileController.storeEntityFile(entityFile);
        } catch(SQLException e){            
            System.out.println("FileUploadServlet: "+e.getMessage());
            return;
        } catch(BackingStoreException |IOException e) {   
            try {
                enityFileController.deleteFileFromDB(entityFile.getOwner(),entityFile.getId());                        // TODO уточнить удаление файла, если он уже был создан
            } catch (SQLException ex) {
                Logger.getLogger(FilesUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("ERROR1");
                return;
            }
            System.out.println("ERROR2");
            return;
        }
        System.out.println("File uploaded");
        resp.print("OK");
    }
}
