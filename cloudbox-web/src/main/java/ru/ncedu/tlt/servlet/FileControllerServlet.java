/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.beans.FileWorkerBean;
import ru.ncedu.tlt.entity.File;
import ru.ncedu.tlt.entity.User;
/**
 *
 * @author Andrey
 */
@WebServlet(name = "FIleControllerServlet", urlPatterns = {"/fileProcess/*"})
public class FileControllerServlet extends HttpServlet{
    
    @EJB 
    FileWorkerBean fileWorker;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
        
        String userID = (String)request.getSession().getAttribute("userName");
        String fileManageRequest = request.getRequestURI().split("/")[request.getRequestURI().split("/").length -1];
        /*
        if (request.getSession().getAttribute("userName") == null){
            request.getRequestDispatcher("login.jsp").forward(request, response);        
        }
        */
        PrintWriter rs = response.getWriter();     
        
        switch (fileManageRequest){
            case "getFilesList":{
                ArrayList<File> filesList = fileWorker.getFilesList(fileManageRequest);
                rs.print("[");
                for(File file: filesList){                    
                    rs.println(file.getJSON());
                    rs.print(",");               
                }
                rs.print("{}]");                  // TODO убрать последнюю запятую
                break;
            }
            case "getFileData": 
            {
                try {
                    Integer fileId = Integer.valueOf(request.getParameter("fileId"));
                    File file = fileWorker.findFile(fileId);
                    JsonBuilderFactory factory = Json.createBuilderFactory(null);
                    JsonObject jO = factory.createObjectBuilder()
                            .add("fileId", file.getId())
                            .add("fileName", file.getName())
                            .add("fileExt", file.getExt())
                            .add("fileDate", String.valueOf(file.getDate()))
                            .add("fileHash", file.getHash())
                            .build();
                    rs.print(jO);
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }       
    }    
}
