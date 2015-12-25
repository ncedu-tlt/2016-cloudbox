/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import ru.ncedu.tlt.entity.File;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author Andrey
 */
@Stateless
@LocalBean
public class FileWorkerBean {           // TODO перенести бин в соответствующий проект, добавить зависимотей
    
    Connection connection;
    
    public boolean deleteFile(String fileID){
        return true;
    }
    
    public ArrayList<File> getFilesList(String userID){
        /*
        TODO : change fake functionality with real
        */
        ArrayList<File> fileList= new ArrayList();
        String sqlQuery = "SELECT * FROM CB_FILE";
        String url = PropertiesCB.CB_JDBC_URL;
        ResultSet result;
         try {
            connection = DriverManager.getConnection(url);
            Statement statement = null;
            statement = connection.createStatement();
            //Выполним запрос
            result = statement.executeQuery(sqlQuery);
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
    }

    public File findFile(String fileName) throws SQLException
    {
        File file = null;
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM CB_FILE WHERE FILENAME = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fileName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                file = new File();
                file.setId(rs.getInt("FILEID"));
                file.setName(rs.getString("FILENAME"));
                file.setExt(rs.getString("FILEEXT"));
                file.setDate(rs.getDate("FILEDATE"));
                file.setHash(rs.getString("FILEHASH"));
            }
        } catch (Exception e) {
            System.out.println("findFile " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return file;
    }

    public File findFile(Integer fileId) throws SQLException
    {
        File file = null;
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM CB_FILE WHERE FILEID = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, fileId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                file = new File();
                file.setId(rs.getInt("FILEID"));
                file.setName(rs.getString("FILENAME"));
                file.setExt(rs.getString("FILEEXT"));
                file.setDate(rs.getDate("FILEDATE"));
                file.setHash(rs.getString("FILEHASH"));
            }
        } catch (Exception e) {
            System.out.println("findFile " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return file;
    }
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