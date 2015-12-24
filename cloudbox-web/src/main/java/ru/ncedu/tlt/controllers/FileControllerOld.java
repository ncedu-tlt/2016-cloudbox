/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ru.ncedu.tlt.entity.File;
import ru.ncedu.tlt.entity.User;

/**
 *
 * @author pavel.tretyakov
 */
public class FileControllerOld {
    
    private Connection dbConnection = null;
    private Statement statement = null;
    private static volatile FileControllerOld instance;
    
//-----Коннект к базе через JDBC
    private Connection getDBConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:cloudbox","cloudbox","cloudbox");
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
    
//----Получение instance контроллера
    public static FileControllerOld getInstance() {
        FileControllerOld localInstance = instance;
        if (localInstance == null) {
            synchronized (FileControllerOld.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FileControllerOld();
                }
            }
        }
        return localInstance;
    }
    
//---Получение файла по айди
    public File getFileById(int fileId)
    {
        File file = new File();
        String query = "select * "
                + "from cb_file "
                + "where fileid = "
                + fileId
                +";";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                file.setId(rs.getInt("FILEID"));
                file.setOwner(rs.getInt("FILEUSERID"));
                file.setName(rs.getString("FILENAME"));
                file.setExt(rs.getString("FILEEXT"));
                file.setDate(rs.getTimestamp("FILEDATE"));
                file.setHash(rs.getString("FILEHASH"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return file;
    }

//---    
    public ArrayList getFileListByExtension(String ext)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_file "
                + "where fileext = '"
                +ext
                +"';";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByExtension(String ext, User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_userfile uf "
                + "inner join cb_file f on uf.uf_fileid = f.fileid "
                + "where f.fileext = '"
                + ext
                + "' and uf.uf_userid = "
                + user.getId()
                + ";";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByDate(String date)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "";
        
        return fileList;
    }
    
//---    
    public ArrayList getFileListByOwner(User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "select * "
                + "from cb_file "
                + "where fileuserid = "
                + user.getId()
                + ";";
        
        return fileList;
    }

//---    
    public ArrayList getFileListByUser(User user)
    {
        File file = new File();
        ArrayList<File> fileList = new ArrayList<>();
        String query = "SELECT * "
                + "FROM CB_FILE "
                + "JOIN CB_USERFILE "
                + "ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID "
                + "WHERE (CB_USERFILE.UF_USERID = "
                + user.getId()
                + ") AND (CB_USERFILE.UF_DEL IS NULL) "
                + "ORDER BY CB_FILE.FILENAME;";
        
        return fileList;
    }


//---    
    public void addFile(File file)
    {
        String query = "insert into cb_file"
                + "values ('"
                + file.getName()
                +"', '"
                + file.getExt()
                +"', "
                + "SYSDATE"
                +", '"
                + file.getHash()
                + "',"
                + file.getOwner()
                + ");";
        
    }

//---    
    public void markAsDeleted(File file)
    {
        String query = "UPDATE CB_USERFILE " +
                "SET UF_DEL = SYSDATE " +
                "WHERE (UF_FILEID = "
                + file.getId()
                + ");";

    }

//---    
    public void markAsDeleted(File file, User user)
    {
        String query = "UPDATE CB_USERFILE " +
                "SET UF_DEL = SYSDATE " +
                "WHERE (UF_FILEID = "
                + file.getId()
                + ") AND (UF_USERID = "
                + user.getId()
                + ");";
        
    }
}
