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

/**
 *
 * @author User
 */
@WebServlet(name = "FilesRestoreServlet", urlPatterns = {"/FilesRestoreServlet"})
public class FilesRestoreServlet extends HttpServlet {
    
    @EJB 
    EntityFileController entityFileController;
    
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
            if(!entityFileController.isOwner(userId, fileId)){
                resp.print("ERROR");
                return;
            }else{ 
                try{
                    entityFileController.restoreFromTrash(userId, fileId);
                }catch(SQLException e){
                   resp.print("ERROR");
                   return;
                }
                System.out.println(" FilesRestoreServlet restore ");
            }            
        }
        resp.print("OK");        
    };
}