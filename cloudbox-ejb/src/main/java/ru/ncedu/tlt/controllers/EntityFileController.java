/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.sql.Connection;
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
import ru.ncedu.tlt.entity.EntityFile;
import ru.ncedu.tlt.hash.HashGenerator;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author victori
 */
@Stateless
@LocalBean
public class EntityFileController {

    @EJB
    HashGenerator hashGenerator;
    
    Connection connection;

    
        public EntityFile createEntityFile(String fileName, Integer ownerId) throws SQLException {    
            
        EntityFile entityFile = new EntityFile();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());     
       
        int indexOfFileExt = fileName.lastIndexOf('.');     //индекс последнего вхождения знака '.' в имени файла           
        entityFile.setName(fileName.substring(0, indexOfFileExt));
        entityFile.setExt(fileName.substring(indexOfFileExt, fileName.length()));    
        entityFile.setDate(timestamp);        
        
        String prehash = entityFile.getName()
                    + entityFile.getExt()
                    + timestamp.toString();
        String hash = hashGenerator.getHash(prehash);
        entityFile.setHash(hash);
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;        
        String sqlQuery = "INSERT INTO CB_FILE "
                + "(FILEID,FILENAME,FILEEXT,FILEDATE,FILEHASH,FILEUSERID)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                keys.next();
                entityFile.setId(keys.getInt(1));
            }           
            preparedStatement.setString(2, entityFile.getName());
            preparedStatement.setString(3, entityFile.getExt());            
            preparedStatement.setString(4, timestamp.toString());
            preparedStatement.setString(5, entityFile.getHash());
            preparedStatement.setInt(6, ownerId);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into CB_FILE table!");
            insertIntoUserFiles(ownerId, entityFile.getId());   // Запись в таблицу CB_USERFILES
            
        } catch (SQLException e) {
            System.out.println("ERROR! createEntityFile: " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return entityFile;
    }
    
//------    
    public Boolean insertIntoUserFiles(Integer idUser, Integer idFile) throws SQLException{     
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "INSERT INTO CB_USERFILE "
                + "(UF_USERID, UF_FILEID, UF_DEL)"
                + "VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idFile);
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into CB_USERFILE table!");
        } catch (SQLException e) {
            System.out.println("ERROR! insertIntoUserFiles : " + e.getMessage());
            return false;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        return true;
    }

//------    
    public Boolean deleteFileToTrash(Integer idUser, Integer idFile) throws SQLException {
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "UPDATE CB_USERFILE" +
                        "SET UF_DEL = SYSDATE" +
                        "WHERE UF_FILEID = ? AND UF_USERID = ?";
        
         try {
            preparedStatement = connection.prepareStatement(sqlQuery);            
            preparedStatement.setInt(1, idFile);
            preparedStatement.setInt(2, idUser);
            preparedStatement.executeUpdate();
            
            System.out.println("File with id=" + idFile + "and user id=" + idUser +" has been deleted");
            
            cleanDependenciesAfterDeleteToTrash(idUser, idFile); //чистка зависимостей после удаления файла в корзину
            
        } catch (SQLException e) {
            System.out.println("ERROR! deleteFileToTrash : " + e.getMessage());
            return false;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        return true;
    }
    
//------    
    public Boolean cleanDependenciesAfterDeleteToTrash(Integer idUser, Integer idFile) throws SQLException {
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "DELETE FROM CB_USERFILE"
                + "WHERE UF_FILEID = ?"
                + "AND UF_USERID IS NOT ?";
         try {
            preparedStatement = connection.prepareStatement(sqlQuery);            
            preparedStatement.setInt(1, idFile);
            preparedStatement.setInt(2, idUser);
            preparedStatement.executeUpdate();
                        
            System.out.println("Deleting dependencies successfully");
        } catch (SQLException e) {
            System.out.println("ERROR! cleanDependenciesAfterDeleteToTrash : " + e.getMessage());
            return false;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
        return true;
    }

//------
    public ArrayList<EntityFile> getMyFilesList(String userID) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "SELECT * FROM CB_FILE" +
                        "JOIN CB_USERFILE ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID" +
                        "WHERE (CB_USERFILE.UF_USERID = ?) AND (CB_USERFILE.UF_DEL IS NULL)" +
                        "AND (CB_FILE.FILEUSERID = CB_USERFILE.UF_USERID)" +            // Только мои файлы
                        "ORDER BY CB_FILE.FILENAME";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp)rs.getObject("FILEDATE"));
                entityFile.setHash(rs.getString("FILEHASH"));
                entityFile.setOwner(rs.getInt("FILEUSERID"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getFilesList: " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return entityFileList;       
    }
    
//------    
    public ArrayList<EntityFile> getMyDeletedFilesList(String userID) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "SELECT * FROM CB_FILE" +
                        "JOIN CB_USERFILE ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID" +
                        "WHERE (CB_USERFILE.UF_USERID = ?) AND (CB_USERFILE.UF_DEL IS NOT NULL)" +
                        "AND (CB_FILE.FILEUSERID = CB_USERFILE.UF_USERID)" +
                        "ORDER BY CB_FILE.FILENAME";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp)rs.getObject("FILEDATE"));
                entityFile.setHash(rs.getString("FILEHASH"));
                entityFile.setOwner(rs.getInt("FILEUSERID"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getFilesList: " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return entityFileList;       
    }
    
//------
    public ArrayList<EntityFile> getAllFiles() throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        
        String sqlQuery = "SELECT * FROM CB_FILE";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp)rs.getObject("FILEDATE"));
                entityFile.setHash(rs.getString("FILEHASH"));
                entityFile.setOwner(rs.getInt("FILEUSERID"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getAllFiles: " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EntityFileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }   
        return entityFileList;
    }
    
//------
    public EntityFile getFileData(Integer fileId) throws SQLException {
        EntityFile entityFile = new EntityFile();

        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;

        String sqlQuery = "SELECT * FROM CB_FILE WHERE FILEID = ?";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp) rs.getObject("FILEDATE"));
                entityFile.setHash(rs.getString("FILEHASH"));
                entityFile.setOwner(rs.getInt("FILEUSERID"));
            }
        } catch (Exception e) {
            System.out.println("ERROR! getFileData: " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return entityFile;
    }
}
