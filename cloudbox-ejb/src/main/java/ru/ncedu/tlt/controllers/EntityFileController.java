/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import ru.ncedu.tlt.entity.File;
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.hash.HashGenerator;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author victori
 */
@Stateless
@LocalBean
public class EntityFileController {           // TODO перенести бин в соответствующий проект, добавить зависимотей

    @EJB
    HashGenerator hashGenerator;
    
    Connection connection;

    public boolean deleteFile(String fileID) {
        return true;
    }

    public ArrayList<File> getFilesList(String userID) {
        /*
        TODO : change fake functionality with real
         */
        ArrayList<File> fileList = new ArrayList();

        String sqlQuery = "SELECT * FROM CB_FILE";
        String url = PropertiesCB.CB_JDBC_URL;
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
        } catch (Exception e) {
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

    public EntityFile createEntityFile(String fileName, Integer ownerId) {       
        
        EntityFile entityFile = new EntityFile();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());        
        
        int indexOfFileExt = fileName.lastIndexOf('.');     //индекс последнего вхождения '.' в имени файла           
        entityFile.setName(fileName.substring(0, indexOfFileExt));
        entityFile.setExt(fileName.substring(indexOfFileExt, fileName.length()));    
        entityFile.setDate(timestamp);        
        
        String prehash = entityFile.getName()
                    + entityFile.getExt()
                    + timestamp.toString();
        String hash = hashGenerator.getHash(prehash);
        entityFile.setHash(hash);
        
        PreparedStatement preparedStatement = null;
        String insertTableSQL = "INSERT INTO CB_FILE "
                + "(FILEID,FILENAME,FILEEXT,FILEDATE,FILEHASH,FILEUSERID)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(insertTableSQL);
            
            preparedStatement.setInt(1, entityFile.getId()); //!Id тут не сгенерено! Нету генератора id!
            preparedStatement.setString(2, entityFile.getName());
            preparedStatement.setString(3, entityFile.getExt());            
            preparedStatement.setString(4, timestamp.toString());
            preparedStatement.setString(5, entityFile.getHash());
            preparedStatement.setInt(6, ownerId);
            
            preparedStatement.executeUpdate();

            System.out.println("retriving new id for file");
//            ResultSet rs = preparedStatement.getGeneratedKeys("file");
//            if (rs.next()) {
//                System.out.println("rs int " + rs.getInt(1));
//                user.setId(rs.getInt(1));
//            }

            //обнуляем пароль
            System.out.println("Record is inserted into CB_FILE table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return entityFile;              //TODO подумать про эксепшн
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return entityFile;
    }
}
