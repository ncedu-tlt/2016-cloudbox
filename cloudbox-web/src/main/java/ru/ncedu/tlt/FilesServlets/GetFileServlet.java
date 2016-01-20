/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.FilesServlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.utils.DiskUtils;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "GetFileServlet", urlPatterns = {"/getFile"})
@MultipartConfig
public class GetFileServlet extends HttpServlet {

    @EJB
    EntityFileController entityFileController;
    
    @EJB
    DiskUtils diskUtils;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = 5;      // TODO    получать настоящий  userID

        EntityFile file;
                
        try{
            int fileId = Integer.parseInt(request.getQueryString().replaceAll("[^0-9]", ""));
            if(!entityFileController.isOwner(userId, fileId)){
                response.getWriter().print("ERROR");
                return;
            };            
             file = entityFileController.getEntityFile(fileId);            
        } catch(NumberFormatException e){
            Logger.getLogger(GetFileServlet.class.getName()).log(Level.SEVERE, null, e);
            response.getWriter().print("NO_PARAMETER");
            return;
        } catch (SQLException e) {
            Logger.getLogger(GetFileServlet.class.getName()).log(Level.SEVERE, null, e);
            response.getWriter().print("ERROR");
            return;
        };
        
        ServletContext ctx = getServletContext();
        String fileFullName = file.getName()+"."+file.getExt();
        String mimeType = ctx.getMimeType(fileFullName);
        response.setContentType(mimeType != null? mimeType:"application/octet-stream");

        response.addHeader("content-disposition", "attachment; filename="+fileFullName);
        OutputStream outStream = response.getOutputStream();

        try {
            diskUtils.writeFileToOutStream(outStream, file.getHash());
        } catch (BackingStoreException ex) {
            Logger.getLogger(GetFileServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().print("ERROR");
        }finally{
            outStream.flush();
            outStream.close();             
        }
    }        
}
