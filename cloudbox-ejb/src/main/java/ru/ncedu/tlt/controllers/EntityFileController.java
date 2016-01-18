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
     * Метод помещает запись об entityFile в БД. Неявно вызывает метод
     * insertEntryInUserFile(ownerId, entityFile.getId()) для записи в таблицу
     * CB_USERFILE
     *
     * @param entityFile
     * @throws SQLException
     */
    public void storeEntityFile(EntityFile entityFile) throws SQLException {
        preparedStatement = null;
        String sqlQuery = "insert into cb_file"
                + "(filename, fileext, filedate, filehash, fileuserid) values"
                + "(?,?,?,?,?)";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entityFile.getName());
            preparedStatement.setString(2, entityFile.getExt());
            preparedStatement.setString(3, entityFile.getDate().toString());
            preparedStatement.setString(4, entityFile.getHash());
            preparedStatement.setInt(5, entityFile.getOwner());

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
            insertEntryInUserFile(entityFile.getOwner(), entityFile.getId());
        } catch (SQLException e) {
            System.out.println("ERROR! storeEntityFile: " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Запись ссылки Пользователь-Файл (USER-FILE) в таблицу CB_USERFILE
     *
     * @param userId - id пользователя
     * @param fileId - id файла
     * @throws SQLException
     */
    public void insertEntryInUserFile(Integer userId, Integer fileId) throws SQLException {
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;
        String sqlQuery = "insert into cb_userfile "
                + "(uf_userid, uf_fileid) values "
                + "(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, fileId);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into CB_USERFILE table!");
        } catch (SQLException e) {
            System.out.println("ERROR! insertEntryInUserFile : " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Помечает файл как удалённый в таблице CB_USERFILE, устанавливая текущую
     * дату в поле UF_DEL.
     *
     * @param userId - id пользователя
     * @param fileId - id файла
     * @throws SQLException
     */
    public void markEntryFileAsTrash(Integer userId, Integer fileId) throws SQLException {
        preparedStatement = null;
        String sqlQuery = "update cb_userfile "
                + "set uf_del = ? "
                + "where uf_fileid = ? "
                + "and uf_userid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, new Timestamp(System.currentTimeMillis()).toString()); //устанавливаю текущее время
            preparedStatement.setInt(2, fileId);
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();
            System.out.println("File with id=" + fileId + "and user id=" + userId + " moved to trash");
        } catch (SQLException e) {
            System.out.println("ERROR! markEntryFileAsTrash : " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Обнуляет поле uf_del в записи по userId, fileId в таблице CB_USERFILE.
     * Тем самым восстанавливая файл из корзины.
     * 
     * @param userId
     * @param fileId
     * @throws SQLException 
     */
    public void restoreFromTrash(Integer userId, Integer fileId) throws SQLException {
        preparedStatement = null;
        String sqlQuery = "update cb_userfile "
                + "set uf_del = null "
                + "where uf_fileid = ? "
                + "and uf_userid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            System.out.println("File with id=" + fileId + "and user id=" + userId + " restore from trash");
        } catch (SQLException e) {
            System.out.println("ERROR! restoreFromTrash : " + e.getMessage());
            throw new SQLException(e);
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
    }
    
    /**
     * Метод осуществляет удаление записей о файлах из таблиц USERFILE и FILE.
     *
     * @param userId - id пользователя запросивщий удаление файла
     * @param fileId - id файла для удаления
     * @throws SQLException
     */
    public void deleteFileFromDB(Integer userId, Integer fileId) throws SQLException {
        if (isOwner(userId, fileId)) { //проверка, является ли пользователь владельцем файла?
            try {
                fullDeleteEntryFromUserfile(fileId);
                deleteEntryFromFile(fileId);
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        } else {
            try {
                onceDeleteEntryFromUserfile(userId, fileId);
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }

    /**
     * Метод проверяет является ли пользователь с userId владельцем файла с fileId.
     *
     * @param userId
     * @param fileId
     * @return boolean, true если является владельцем
     */
    public boolean isOwner(Integer userId, Integer fileId) {
        preparedStatement = null;
        String sqlQuery = "select *"
                + " from cb_file f"
                + " where f.fileid = ?"
                + " and f.fileuserid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            preparedStatement.setInt(2, userId);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();   //если результат выборки не нулевой, возвращает true
        } catch (SQLException e) {
            System.out.println("ERROR! checkOwner : " + e.getMessage());
            return false;
        }
    }

    /**
     * Удаляет одну ссылку на файл из CB_USERFILE.
     *
     * @param userId - id пользователя
     * @param fileId - id файла для удаления
     * @throws SQLException
     */
    public void onceDeleteEntryFromUserfile(Integer userId, Integer fileId) throws SQLException {
        preparedStatement = null;
        String sqlQuery = "delete from cb_userfile "
                + "where uf_fileid = ? "
                + "and uf_userid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            preparedStatement.setInt(2, userId);           
            preparedStatement.executeUpdate();
            System.out.println("onceDeleteUserfile succesfull, userId : " + userId + "fileId : " + fileId);
        } catch (SQLException e) {
            System.out.println("ERROR! onceDeleteUserfile : " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Удаляет все ссылки на файл из CB_USERFILE.
     *
     * @param fileId - id файла для удаления
     * @throws SQLException
     */
    public void fullDeleteEntryFromUserfile(Integer fileId) throws SQLException {
        preparedStatement = null;
        String sqlQuery = "delete from cb_userfile "
                + "where uf_fileid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            preparedStatement.executeUpdate();
            System.out.println("fullDeleteUserfile succesfull, fileId : " + fileId);
        } catch (SQLException e) {
            System.out.println("ERROR! fullDeleteEntryFromUserfile : " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Удаляет ссылку на файл из CB_FILE.
     *
     * @param fileId - id файла для удаления
     * @throws SQLException
     */
    public void deleteEntryFromFile(Integer fileId) throws SQLException {
        preparedStatement = null;

        String sqlQuery = "delete from cb_file "
                + "where fileid = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            preparedStatement.executeUpdate();
            System.out.println("deleteFile succesfull, fileId : " + fileId);
        } catch (SQLException e) {
            System.out.println("ERROR! deleteEntryFromFile : " + e.getMessage());
            throw new SQLException(e);
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
    }

    /**
     * Метод возващает список файлов загруженных пользователем. Исключая файлы
     * из корзины (удалённые).
     *
     * @param userId
     * @return ArrayList&lt;EntityFile&gt; - список файлов в виде коллекции
     * EntityFile
     * @throws SQLException
     */
    public ArrayList<EntityFile> getUserFiles(String userId) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        preparedStatement = null;
        String sqlQuery = "select * from cb_file f "
                + "join cb_userfile uf on f.fileid = uf.uf_fileid "
                + "where uf.uf_userid = ? " 
                + "and uf.uf_del is null " 
                + "and f.fileuserid = uf.uf_userid "
                + "order by f.filename";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("fileid"));
                entityFile.setName(rs.getString("filename"));
                entityFile.setExt(rs.getString("fileext"));
                entityFile.setDate((Timestamp) rs.getObject("filedate"));
                entityFile.setHash(rs.getString("filehash"));
                entityFile.setOwner(rs.getInt("fileuserid"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getUserFiles: " + e.getMessage());
            throw new SQLException(e);
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
     * @param userId
     * @return ArrayList&lt;EntityFile&gt; - список файлов в виде коллекции
     * EntityFile
     * @throws SQLException
     */
    public ArrayList<EntityFile> getUserFilesInTrash(String userId) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        preparedStatement = null;
        String sqlQuery = "select * from cb_file f "
                + "join cb_userfile uf on f.fileid = uf.uf_fileid "
                + "where uf.uf_userid = ? " 
                + "and uf.uf_del is not null " 
                + "and f.fileuserid = uf.uf_userid "
                + "order by f.filename";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("fileid"));
                entityFile.setName(rs.getString("filename"));
                entityFile.setExt(rs.getString("fileext"));
                entityFile.setDate((Timestamp) rs.getObject("filedate"));
                entityFile.setHash(rs.getString("filehash"));
                entityFile.setOwner(rs.getInt("fileuserid"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getFilesList: " + e.getMessage());
            throw new SQLException(e);
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
     * Возвращает список всех файлов из таблицы CB_USER. Используется на
     * странице админки
     *
     * @return ArrayList&lt;EntityFile&gt; - список файлов в виде коллекции
     * EntityFile
     * @throws SQLException
     */
    public ArrayList<EntityFile> getAllFiles() throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        preparedStatement = null;
        String sqlQuery = "select * from cb_file";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("fileid"));
                entityFile.setName(rs.getString("filename"));
                entityFile.setExt(rs.getString("fileext"));
                entityFile.setDate((Timestamp) rs.getObject("filedate"));
                entityFile.setHash(rs.getString("filehash"));
                entityFile.setOwner(rs.getInt("fileuserid"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getAllFiles: " + e.getMessage());
            throw new SQLException(e);
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
     * Метод возвращает файлы, которые расшарили пользователю
     *
     * @param userId
     * @return ArrayList&lt;EntityFile&gt;
     * @throws SQLException
     */
    public ArrayList<EntityFile> getSharedUserFiles(String userId) throws SQLException {
        ArrayList<EntityFile> entityFileList = new ArrayList();
        preparedStatement = null;
        String sqlQuery = "select f.* from cb_userfile uf, cb_file f "
                + "where uf.uf_fileid = f.fileid "
                + "and uf.uf_userid = ? "
                + "and uf.uf_userid != f.fileuserid";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                EntityFile entityFile = new EntityFile();
                entityFile.setId(rs.getInt("fileid"));
                entityFile.setName(rs.getString("filename"));
                entityFile.setExt(rs.getString("fileext"));
                entityFile.setDate((Timestamp) rs.getObject("filedate"));
                entityFile.setHash(rs.getString("filehash"));
                entityFile.setOwner(rs.getInt("fileuserid"));
                entityFileList.add(entityFile);
            }
        } catch (Exception e) {
            System.out.println("ERROR! getSharedUserFiles : " + e.getMessage());
            throw new SQLException(e);
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
    public EntityFile getEntityFile(Integer fileId) throws SQLException {
        EntityFile entityFile = new EntityFile();
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;
        String sqlQuery = "select * from cb_file where fileid = ?";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, fileId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                entityFile.setId(rs.getInt("fileid"));
                entityFile.setName(rs.getString("filename"));
                entityFile.setExt(rs.getString("fileext"));
                entityFile.setDate((Timestamp) rs.getObject("filedate"));
                entityFile.setHash(rs.getString("filehash"));
                entityFile.setOwner(rs.getInt("fileuserid"));
            }
        } catch (Exception e) {
            System.out.println("ERROR! getEntityFile: " + e.getMessage());
            throw new SQLException(e);
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
    
    /**
     * Обновление данных о файле
     * Используется на странице администратора
     * 
     * @param fileId
     * @param column поле в БД которое требует апдейта
     * @param value новое значение поля 
     * @throws SQLException
     */
    public void updateFileData(Integer fileId, String column, String value) throws SQLException {
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;
        String sqlQuery = "update cb_file "
                + "set " + column + " = ? "
                + "where fileid = ?";
        try {
            preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setString(1, column);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, fileId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR! updateFileData: " + e.getMessage());
            throw new SQLException(e);
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
    }
    
}
