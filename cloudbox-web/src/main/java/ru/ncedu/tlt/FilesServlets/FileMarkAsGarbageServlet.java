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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        Integer userId = (Integer) request.getSession().getAttribute("userId");       
        
        ArrayList<Integer> listFileId = Service.getIntListFromJSONlist(request.getHeader("listIdFiles")); // TODO to standart library
        PrintWriter resp = response.getWriter(); 
        
        if(listFileId.isEmpty()){
            resp.print("NO_PARAMETERS");
            return;
        }
        try {
            entityFileController.markEntryFileAsTrash(listFileId, userId);
        } catch (SQLException ex) {
            System.out.println("ERROR FileMarkAsGarbageServlet: "+ ex);
        }
        resp.print("OK");
//        
//        for(int fileId: listFileId){
//            if(!entityFileController.isOwner(userId, fileId)){
//                resp.print("NOT AN OWNER");
//                return;
//            }else{                
//                try{
//                    entityFileController.markEntryFileAsTrash(userId,fileId);
//                }catch(SQLException e){
//                   resp.print("CANT MARK AS DELETED."+e.getMessage());
//                   return;
//                }
//            }            
//        }
//        resp.print("OK");        
    }
}