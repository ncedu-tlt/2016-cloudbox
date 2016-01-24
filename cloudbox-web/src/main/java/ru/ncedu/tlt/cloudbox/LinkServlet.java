/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "LinkDeleteServlet", urlPatterns = {"/linkServlet/*"})
public class LinkServlet extends HttpServlet {
   
    @EJB 
    EntityFileController entityFileController;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int userId = (Integer) request.getSession().getAttribute("userId");
        //int userID = Integer.getInteger((String)request.getSession().getAttribute("userName")); 
        
        String queryType = request.getRequestURI().split("/")[request.getRequestURI().split("/").length -1];
        
        ArrayList<Integer> listFileId = Service.getIntListFromJSONlist(request.getHeader("listIdFiles")); // TODO to standart library
        PrintWriter rs = response.getWriter(); 
        
        if(listFileId.isEmpty()){
            rs.print("NO_PARAMETER");
            return;
        }
        
        for(int fileId: listFileId){
            if(!entityFileController.isOwner(userId, fileId)){
                rs.print("ERROR");
                return;
            }else{   
                try{
                    switch(queryType){
                        case "shareToUser":{
                            //filesList  = enityFileController.getMyFilesList(userId);   
                            break;
                        }
                        case "makeExternalLink":{
                            //filesList  = enityFileController.getMyFilesList(userId);   
                            break;
                        }
                        case "deleteSharedLink":{
                            //filesList  = enityFileController.getMyDeletedFilesList(userId);
                            break;
                        }
                    }
                }catch(Exception ex){  
                    rs.print("ERROR");
                    return;
                }
            }            
        }
        rs.println("OK");        
    };
}
