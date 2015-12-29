/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
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

    private ResultSet manageQuery(String sqlQuery, String type) throws SQLException{
        System.out.println("try to get connection");
        String url = "jdbc:derby://localhost:1527/cloudbox";
        Connection connection = null;
        ResultSet result = null;

         try {
            connection = DriverManager.getConnection(url);
            Statement statement = null;

            statement = connection.createStatement();
            if(type.equals("SELECT")) result = statement.executeQuery(sqlQuery);
            else if(type.equals("UPDATE")) statement.executeUpdate(sqlQuery);
         }
         catch(SQLException e){           
             e.printStackTrace();
             throw new SQLException(e);
         }
         return result;
    };    
    
    
    
    public ArrayList<File> getFilesList(String userID){
        ArrayList<File> fileList= new ArrayList();
        
        String sqlQuery = "SELECT * FROM CB_FILE";        
        
            
            try{
                ResultSet queryResult = manageQuery(sqlQuery, "SELECT");
                while (queryResult.next()) {
                    File file = new File();
                    file.setId(queryResult.getInt("fileid"));
                    file.setName(queryResult.getString("filename"));
                    file.setExt(queryResult.getString("fileext"));
                    file.setDate(queryResult.getDate("filedate"));
                    fileList.add(file);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            };
        return fileList;
    };    
    
    public String deleteFile(ArrayList<Integer> fileID, int userId){ // TODO добавить проверку валидности пользователя 
                                                                     //на случай создания фейкового запроса 
        String result = "";
        
        try {
                String sqlQuery_1 = "SELECT FILEHASH FROM APP.CB_FILE WHERE FILEID = %d";
                String sqlQuery_2 = "DELETE FROM APP.CB_FILE WHERE FILEID = %d";
                result = result.concat("delete fileID: " + fileID.toString() + "\n");
                for(int fileId: fileID){
                    String getFileNameQuery = String.format(sqlQuery_1, fileId);
                    ResultSet queryResult = manageQuery(getFileNameQuery, "SELECT");
                    queryResult.next(); 
                    String fileTrueName = queryResult.getString("FILEHASH");
                    System.out.println(fileTrueName);
                    

                    if(!DiscManager.deleteFile(fileTrueName)) result = result.concat("with file: "+fileId +"problem while deleting  ");
                    else{
                        System.out.println("drop file from base");
                        String deleteFileQuery = String.format(sqlQuery_2, fileId);
                        queryResult = manageQuery(deleteFileQuery, "UPDATE");
                    }
                }
                result = ("OK");                         
            }
        catch(Exception e){ 
            result = e.getMessage();
        }
        return result;
    }
    
    public File getFileMeta(int fileId){
        File file = null;
        try {
            String sqlQuery_1 = "SELECT * FROM APP.CB_FILE WHERE FILEID = %d";
            String getFileNameQuery = String.format(sqlQuery_1, fileId);
            ResultSet queryResult = manageQuery(getFileNameQuery, "SELECT");
            queryResult.next();             
            
            file = new File();
            file.setId(fileId);
            file.setName(queryResult.getString("FILENAME"));
            file.setExt(queryResult.getString("FILEEXT"));
            file.setDate(queryResult.getTimestamp("FILEDATE"));
            file.setHash(queryResult.getString("FILEHASH"));  
            return file;
        }
        catch(Exception e){
            file = null;
            System.out.println(e.getMessage());
        }
        return file;        
    }
    
    public String sendFile(OutputStream outStream, File file){
        
        String result =null;
        
        BufferedInputStream fileStream;        
        try {
            fileStream = DiscManager.getFileStream(file.getHash());
            byte[] buffer = new byte[1024];
            while(true){                
                int numBuffBytes = fileStream.read(buffer);                
                if(numBuffBytes == -1) break;
                outStream.write(buffer, 0, numBuffBytes);
            }
            result = "OK";
        } catch (FileNotFoundException e) {
            result = e.getMessage();
        } catch (IOException e) {
            result = e.getMessage();
        }       
        return result;   
    }
    
    public String storeFile(File file, int userID, InputStream fileStream){

        String result = "";       
        
        try {
                String uuidName = Service.getRandomUUID();                
                String newFileName = DiscManager.storeFile(fileStream, uuidName);
                
                if(newFileName!=null){ // TODO продумать когда может вернуть NULL 
                    String sqlQuery_1 = "SELECT FILEID FROM CB_FILE ORDER BY FILEID DESC";         // TODO переписать с GROUP и MAX
                    String sqlQuery_2 = "insert into CB_FILE values (%d,%d,\'%s\',\'%s\',\'%s\',\'%s\')"; 
                    ResultSet queryResult = manageQuery(sqlQuery_1, "SELECT");
                    
                    queryResult.next();                             // TODO fileId просто инкрементируется. Но нужно искать первый свободный номер.
                                                                            // могут быть проблемы с многопоточностью
                    int maxFileID = queryResult.getInt("fileid");    //берем последний с конца см. sql

                    String fileDate = Service.getCurrentTimeStamp(); 
                    sqlQuery_2 = String.format(sqlQuery_2, userID , (maxFileID+1) , file.getName() , file.getExt(),fileDate,newFileName);
                    result = sqlQuery_2;

                    queryResult = manageQuery(sqlQuery_2,"UPDATE");                   
                    result = "OK";                       
                }                                
            }
        catch(SQLException e){            
            e.printStackTrace();
            result = "caught SQLException \n";
        }
        catch(BackingStoreException e){
            result = e.getMessage() + "Storage is full \n";
        }
        catch(IOException e){
            result = e.getMessage() +"Can't create file \n";
        }
        catch(Exception e){ 
            result = e.getMessage();
        }
        return result;
    }
}
