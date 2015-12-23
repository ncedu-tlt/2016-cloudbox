/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import ru.ncedu.tlt.entity.File;

/**
 *
 * @author Andrey
 */
@Stateless
@LocalBean
public class FileWorkerBean {           // TODO перенести бин в соответствующий проект, добавить зависимотей
    
    
    public boolean deleteFile(String fileID){
        return true;
    }
    
    public ArrayList<File> getFilesList(String userID){
        /*
        TODO : change fake functionality with real
        */
        ArrayList<File> fileList= new ArrayList();
        
        String sqlQuery = "SELECT * FROM CB_FILE";
        String url = "jdbc:derby://localhost:1527/cloudbox";
        Connection connection = null;
        ResultSet result;
                
         try {
            System.out.println("try to get connection");
            connection = DriverManager.getConnection(url);
            Statement statement = null;

            statement = connection.createStatement();
            //Выполним запрос
            result = statement.executeQuery(sqlQuery);
            
            System.out.println("Выводим statement");
            while (result.next()) {
                File file = new File();
                
                file.setId(result.getInt("fileid"));
                file.setName(result.getString("filename"));
                file.setExt(result.getString("fileext"));
                file.setDate(result.getDate("filedate"));
                fileList.add(file);
            }                               
         }
         catch(Exception e){
             System.out.println(e.getMessage());
             e.printStackTrace();
         }
        return fileList;
       
        // <FAKE>
        /*
        File file1 = new File();
        file1.setId(112);
        file1.setExt("txt");
        file1.setName("The Book");
        file1.setDate(new Date(1988,5,1));        
        fileList.add(file1);
        
        File file2 = new File();
        file2.setId(285);
        file2.setExt("mp3");
        file2.setName("Drain");
        file2.setDate(new Date(1999,2,6));        
        fileList.add(file2);
        
        return fileList;
        */
    }
}