/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import com.google.gson.Gson;
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
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
/**
 *
 * @author Andrey
 */
@WebServlet(name = "FIleControllerServlet", urlPatterns = {"/fileProcess/*"})
public class FileControllerServlet extends HttpServlet{
    
    @EJB 
    EntityFileController fileWorker;
    
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
        String fileManageRequest = request.getRequestURI().split("/")[request.getRequestURI().split("/").length -1];
        /*
        if (request.getSession().getAttribute("userName") == null){
            request.getRequestDispatcher("login.jsp").forward(request, response);        
        }
        */
        PrintWriter rs = response.getWriter();     
        
        switch (fileManageRequest){
            case "getFilesList":
            {
                try 
                {
                    Gson gson = new Gson();
                    ArrayList filesList = fileWorker.getAllFiles();
                    rs.print(gson.toJson(filesList));
                    break;
                } catch (SQLException ex) 
                {
                    System.out.println(ex);
                }
            }
            case "getFileData":
            {
                try 
                {
                    Integer fileId = Integer.valueOf(request.getParameter("fileId"));
                    EntityFile file = fileWorker.getFileData(fileId);
                    Gson gson = new Gson();
                    rs.print(gson.toJson(file));
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }       
    }    
}
