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
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.FilesServlets.Service;
import ru.ncedu.tlt.controllers.EntityFileController;

@WebServlet(name = "FileMarkAsGarbageServlet", urlPatterns = {"/markFilesAsGarbage"})
public class FileMarkAsGarbageServlet extends HttpServlet {
    
    @EJB 
    EntityFileController entityFileController;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userId = "5";  // TODO сменить на реальный из сессии
        //int userID = Integer.getInteger((String)request.getSession().getAttribute("userName")); 
        
        ArrayList<Integer> listFileId = Service.getIntListFromJSONlist(request.getHeader("listIdFiles")); // TODO to standart library
        PrintWriter resp = response.getWriter(); 
        
        if(listFileId.isEmpty()){
            resp.print("NO_PARAMETER");
            return;
        }
        
        for(int fileId: listFileId){
            if(!entityFileController.isOwner(Integer.parseInt(userId), fileId)){
                resp.print("ERROR");
                return;
            }else{                
                try{
                    entityFileController.markEntryFileAsTrash(Integer.parseInt(userId),fileId);
                }catch(SQLException e){
                   resp.print("ERROR");
                   return;
                }
            }            
        }
        resp.print("OK");        
    };
}