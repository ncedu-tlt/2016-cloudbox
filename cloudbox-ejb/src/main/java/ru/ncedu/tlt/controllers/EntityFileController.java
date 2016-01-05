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

    PreparedStatement preparedStatement;
    Connection connection;

    /**
     * Осуществляет запись данных о файле а CB_FILE.
     * Неявно вызывает метод insertIntoUserFiles(Integer idUser, Integer idFile)
     * для записи в таблицу CB_USERFILE
     * 
     * @param fileName - название файла (документа)
     * @param ownerId - id владельца файла
     * @return объект типа EntityFile
     * @throws SQLException 
     */
    public EntityFile createEntityFile(String fileName, Integer ownerId) throws SQLException {
        EntityFile entityFile = new EntityFile();       
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        int indexOfFileExt = fileName.lastIndexOf('.');     //индекс последнего вхождения знака '.' в имени файла           
        entityFile.setName(fileName.substring(0, indexOfFileExt));
        entityFile.setExt(fileName.substring(indexOfFileExt, fileName.length()));
        entityFile.setDate(timestamp);

        //генерация хэша из имени файла
        String prehash = entityFile.getName()
                + entityFile.getExt()
                + timestamp.toString();
        String hash = hashGenerator.getHash(prehash);
        entityFile.setHash(hash);
        
        preparedStatement = null;
        
        String sqlQuery = "INSERT INTO CB_FILE "
                + "(FILENAME, FILEEXT, FILEDATE, FILEHASH, FILEUSERID) VALUES "
                + "(?,?,?,?,?)";
        try {     
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, entityFile.getName());
            preparedStatement.setString(2, entityFile.getExt());
            preparedStatement.setString(3, timestamp.toString());
            preparedStatement.setString(4, entityFile.getHash());
            preparedStatement.setInt(5, ownerId);
            
            preparedStatement.executeUpdate();
            //ERROR! createEntityFile: Column 'FILEID'  cannot accept a NULL value.
            //почему-то не работает генератор Statement.RETURN_GENERATED_KEYS 
            //с таблицей CB_FILE
            
            System.out.println("retriving new id for file");
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                keys.next();      
                
                entityFile.setId(keys.getInt(1));
            }      
            
            System.out.println("Record is inserted into CB_FILE table!");  
            
            // Запись в таблицу CB_USERFILES
            insertIntoUserFiles(ownerId, entityFile.getId());  
            
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

    /**
     * Запись ссылки Пользователь-Файл (USER-FILE) в таблицу CB_USERFILE
     * 
     * @param idUser
     * @param idFile
     * @return  true - если запись прошла успешно
     * @throws SQLException 
     */
    public Boolean insertIntoUserFiles(Integer idUser, Integer idFile) throws SQLException {

        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;

        String sqlQuery = "INSERT INTO CB_USERFILE "
                + "(UF_USERID, UF_FILEID) VALUES"
                + "(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idFile);
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

    /**
     * Помечает файл как удалённый в таблице CB_USERFILE, устанавливая текущую
     * дату в поле UF_DEL. Тем самым отмечая файл как помещённый в корзину
     * Неявно вызывает метод cleanDependenciesAfterDeleteToTrash(idUser, idFile)
     * для удаления ссылок на файл помещённый владельцем в корзину из таблицы
     * CB_USERFILE
     *
     * @param idUser
     * @param idFile
     * @return true - если запись прошла успешно
     * @throws SQLException
     */
    public Boolean deleteFileToTrash(Integer idUser, Integer idFile) throws SQLException {
        
        preparedStatement = null;

        String sqlQuery = "UPDATE CB_USERFILE"
                + "SET UF_DEL = SYSDATE"
                + "WHERE UF_FILEID = ? AND UF_USERID = ?";

        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idFile);
            preparedStatement.setInt(2, idUser);
            preparedStatement.executeUpdate();

            System.out.println("File with id=" + idFile + "and user id=" + idUser + " has been deleted");

            //чистка зависимостей после удаления файла в корзину
            cleanDependenciesAfterDeleteToTrash(idUser, idFile);

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

    /**
     * Удаляет ссылки на файл idFile исключая юзера idUser.
     * Используется для чистки зависимостей в CB_USERFILE после удаления файла владельцем.
     * 
     * @param idUser
     * @param idFile
     * @return true - если операция прошла успешно
     * @throws SQLException 
     */    
    public Boolean cleanDependenciesAfterDeleteToTrash(Integer idUser, Integer idFile) throws SQLException {

        
        preparedStatement = null;

        String sqlQuery = "DELETE FROM CB_USERFILE"
                + "WHERE UF_FILEID = ?"
                + "AND UF_USERID IS NOT ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
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

    /**
     * Метод возващает список файлов загруженных пользователем.
     * Исключая файлы из корзины (удалённые).
     * 
     * @param userID
     * @return ArrayList<EntityFile> - список файлов в виде коллекции EntityFile
     * @throws SQLException 
     */
    public ArrayList<EntityFile> getMyFilesList(String userID) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();

        preparedStatement = null;

        String sqlQuery = "SELECT * FROM CB_FILE"
                + "JOIN CB_USERFILE ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID"
                + "WHERE (CB_USERFILE.UF_USERID = ?) AND (CB_USERFILE.UF_DEL IS NULL)"
                + "AND (CB_FILE.FILEUSERID = CB_USERFILE.UF_USERID)" + // Только мои файлы
                "ORDER BY CB_FILE.FILENAME";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp) rs.getObject("FILEDATE"));
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

    /**
     * Метод возващает список файлов пользователя находящихся в корзине.
     * 
     * @param userID
     * @return ArrayList<EntityFile> - список файлов в виде коллекции EntityFile
     * @throws SQLException
     */
    public ArrayList<EntityFile> getMyDeletedFilesList(String userID) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
    
        preparedStatement = null;

        String sqlQuery = "SELECT * FROM CB_FILE"
                + "JOIN CB_USERFILE ON CB_FILE.FILEID = CB_USERFILE.UF_FILEID"
                + "WHERE (CB_USERFILE.UF_USERID = ?) AND (CB_USERFILE.UF_DEL IS NOT NULL)"
                + "AND (CB_FILE.FILEUSERID = CB_USERFILE.UF_USERID)"
                + "ORDER BY CB_FILE.FILENAME";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);           
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userID);            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp) rs.getObject("FILEDATE"));
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

    /**
     * Возвращает список всех файлов из таблицы CB_USER.
     * Используется на странице админки
     *  
     * @return ArrayList<EntityFile> - список файлов в виде коллекции EntityFile
     * @throws SQLException 
     */
    public ArrayList<EntityFile> getAllFiles() throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        
        preparedStatement = null;

        String sqlQuery = "SELECT * FROM CB_FILE";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);            
            preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("FILEID"));
                entityFile.setName(rs.getString("FILENAME"));
                entityFile.setExt(rs.getString("FILEEXT"));
                entityFile.setDate((Timestamp) rs.getObject("FILEDATE"));
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

    /**
     * Метод возвращает EntityFile по id.
     * 
     * @param fileId
     * @return EntityFile
     * @throws SQLException 
     */
    public EntityFile getFileData(Integer fileId) throws SQLException {
        EntityFile entityFile = new EntityFile();

        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;

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
