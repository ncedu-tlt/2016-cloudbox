/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.FilesServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
/**
 *
 * @author Andrey
 */
@WebServlet(name = "FilesListServlet", urlPatterns = {"/getFileList/*"})
public class FilesListServlet extends HttpServlet{
    
    @EJB
    EntityFileController enityFileController;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        
        Integer userId = 4;      // TODO    получать настоящий  userID
        //String userId = (String)request.getSession().getAttribute("userName");
        
        String typeFilesList = request.getRequestURI().split("/")[request.getRequestURI().split("/").length -1];
        
        PrintWriter rs = response.getWriter();     
        ArrayList<EntityFile> filesList = null;
        
        try{
            switch(typeFilesList){
                case "ownedFiles":{
                    filesList  = enityFileController.getUserFiles(userId);   
                    break;
                }
                case "deletedFiles":{
                    filesList  = enityFileController.getUserFilesInTrash(userId);
                    break;
                }
                case "sharedFiles":{
                    filesList  = enityFileController.getSharedUserFiles(userId);                    
                }
            }
        }catch(Exception ex){  
            rs.print("ERROR");
            return;
        }

        StringBuffer filesJson = new StringBuffer("[");
        for(EntityFile file: filesList){                    
            filesJson.append(file.getJSON());
            filesJson.append(",");               
        }
        filesJson.deleteCharAt(filesJson.length()-1).append("]");
        rs.print(filesJson.toString());
    }    
}
