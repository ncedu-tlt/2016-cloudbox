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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.utils.DiskUtils;
/**
 *
 * @author pavel.tretyakov
 */
@WebServlet(name = "FileAdministrationServlet", urlPatterns = {"/fileProcess/*"})
public class FileControllerServlet extends HttpServlet{

    @EJB
    private DiskUtils diskUtils;
    
    @EJB 
    EntityFileController entityFileController;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (request.getSession().getAttribute("userName") == null){
            request.getRequestDispatcher("login.jsp").forward(request, response);        
        }
        executeCommand(request, response);
        
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
        executeCommand(request, response);
    }

    protected void executeCommand(HttpServletRequest request, HttpServletResponse response)
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
            case "getAllFiles":
            {
                try 
                {
                    Gson gson = new Gson();
                    rs.print(gson.toJson(entityFileController.getAllFiles()));
                    break;
                } catch (SQLException ex) 
                {
                    System.out.println(ex);
                }
            }
            case "getUserFiles":
            {
                try 
                {
                    Integer userId = Integer.valueOf(request.getParameter("userId"));
                    Gson gson = new Gson();
                    rs.print(gson.toJson(entityFileController.getUserFiles(userId)));
                    break;
                } catch (SQLException ex) 
                {
                    System.out.println(ex);
                }
            }
            case "getSharedUserFiles":
            {
                try 
                {
                    Integer userId = Integer.valueOf(request.getParameter("userId"));
                    Gson gson = new Gson();
                    rs.print(gson.toJson(entityFileController.getSharedUserFiles(userId)));
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
                    EntityFile file = entityFileController.getEntityFile(fileId);
                    Gson gson = new Gson();
                    rs.print(gson.toJson(file));
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            case "checkFile":
            {
            try {
                Integer fileId = Integer.valueOf(request.getParameter("fileId"));
                EntityFile file = entityFileController.getEntityFile(fileId);
                rs.print(diskUtils.checkFileOnDisk(file.getHash()));
                break;
            } catch (SQLException ex) {
                Logger.getLogger(FileControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            case "updateFileData":
            {
                try 
                {
                    Integer fileId = Integer.valueOf(request.getParameter("fileId"));
                    String column = request.getParameter("column");
                    String value = request.getParameter("value");
                    entityFileController.updateFileData(fileId, column, value);
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }       
    }    
}
