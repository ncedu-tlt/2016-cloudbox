/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import ru.ncedu.tlt.entity.File;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "GetFileServlet", urlPatterns = {"/getFile"})
@MultipartConfig
public class GetFileServlet extends HttpServlet {

    @EJB 
    FileWorkerBean fileWorker;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        int fileId = Integer.parseInt(request.getQueryString().replaceAll("[^0-9]", ""));

        File file = fileWorker.getFileMeta(fileId);
        
        /*
        PrintWriter resp = response.getWriter();        
        if(file==null) resp.print("BAD file ");
        else resp.print("file.getName():  "+file.getName()+"file.getExt():  "+file.getExt()+
                "file.getHash():  "+file.getHash()); 
        */
        
        ServletContext ctx = getServletContext();
        String fileFullName = file.getName()+"."+file.getExt();
        String mimeType = ctx.getMimeType(fileFullName);
        response.setContentType(mimeType != null? mimeType:"application/octet-stream");

        response.addHeader("content-disposition", "attachment; filename="+fileFullName);
        OutputStream outStream = response.getOutputStream();
        
        String result = fileWorker.sendFile(outStream, file);
        outStream.flush();
        outStream.close(); 
        if (! result.equals("OK")){
             PrintWriter wr = response.getWriter();
             wr.write(result);
        }
    }        
}
