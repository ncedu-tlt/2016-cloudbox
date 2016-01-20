/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.FilesServlets.GetFileServlet;
import ru.ncedu.tlt.controllers.EntityFileController;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.utils.DiskUtils;

@WebServlet(name = "ExternalLinkServiceServlet", urlPatterns = {"/getSharedFile/"})
public class ExternalLinkServiceServlet extends HttpServlet {

    @EJB
    EntityFileController entityFileController;
    
    @EJB
    DiskUtils diskUtils;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fileUUID = request.getRequestURI().split("/")[request.getRequestURI().split("/").length -1];
        
        EntityFile file;
        
        try {
//            file = entityFileController.getEntityFile(getIdEntityFileByLink(fileUUID));   NOT IMPLEMENTED
              file = entityFileController.getEntityFile(5);   // plug  DELETE
        } catch (SQLException ex) {
            Logger.getLogger(ExternalLinkServiceServlet.class.getName()).log(Level.SEVERE, null, ex); 
            // throw  404
            return;
        }
        
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
            // throw  404
        }finally{
            outStream.flush();
            outStream.close();             
        }
        
    }

}
