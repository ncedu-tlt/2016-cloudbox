/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.FilesServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.FilesServlets.Service;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.utils.DiskUtils;

/**
 *
 * Сервлет удаляет файл полностью 
 * 
 */
@WebServlet(name = "FileDeleteServlet", urlPatterns = {"/deleteFiles"})
public class FileDeleteServlet extends HttpServlet {
    
    @EJB 
    EntityFileController entityFileController;
    
    @EJB
    DiskUtils diskUtils;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Integer userId = (Integer) request.getSession().getAttribute("userId");
       
        ArrayList<Integer> listFileId = Service.getIntListFromJSONlist(request.getHeader("listIdFiles")); // TODO to standart library
        PrintWriter resp = response.getWriter();        
  
        if(listFileId.isEmpty()){
            resp.print("NO_PARAMETER");
            return;
        }
        
        for(int fileId: listFileId){ 
            EntityFile entityFile = null;
            if(!entityFileController.isOwner(userId, fileId)){
                response.getWriter().print("ERROR");
                return;
            }else{                
                try{
                    entityFile = entityFileController.getEntityFile(fileId);  
                    entityFileController.deleteFileFromDB(userId , fileId);                  
                    diskUtils.deleteFile(entityFile.getHash());
                }catch(SQLException | BackingStoreException e){   // TODO подчищать хвосты на диске и/ или в базе в случае креша 
                   resp.print("ERROR");
                   return;
                }
            }            
        }
        resp.print("OK");
    }
}
