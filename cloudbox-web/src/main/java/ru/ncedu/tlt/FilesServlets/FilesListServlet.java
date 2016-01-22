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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (request.getSession().getAttribute("userName") == null){
            request.getRequestDispatcher("login.jsp").forward(request, response);        
        }
        
        PrintWriter rs = response.getWriter();
        rs.print(request.getRequestURI().split("/"));
        
/*        
                    request.getSession().setAttribute("userName", user.getName());
            request.getSession().setAttribute("userId", user.getId());
            response.sendRedirect("drive.jsp");
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        */
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Integer userId = (Integer) request.getSession().getAttribute("userId");      // TODO    получать настоящий  userID
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
