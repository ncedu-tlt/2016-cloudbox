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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.entity.UserRole;
import ru.ncedu.tlt.hash.HashGenerator;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author Andrew
 */
@Stateless
@LocalBean
public class UserController {

    @EJB
    private RoleController rC;

    @EJB
    HashGenerator hg;

    @Resource(name = "jdbc/CBDataSource", type = javax.sql.ConnectionPoolDataSource.class)
    private DataSource dataSource;

    Connection connection;
    PreparedStatement preparedStatement;

    public UserController() {
    }

    public User createUser(User user) {
        user.setHash(hg.getHash(user.getPass()));
        user.setPass("");
        System.out.println("getted userhash");

        preparedStatement = null;

        String insertTableSQL = "INSERT INTO CB_USER"
                + "(USERMAIL, USERPASSHASH, USERSALT, USERNAME, USERNOTES, USERPIC) VALUES"
                + "(?,?,?,?,?,?)";

        int key = 0;
        try {
            connection = dataSource.getConnection();
//            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getHash());
            preparedStatement.setString(3, "user.getSalt");
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, "user.getNotes");
            preparedStatement.setString(6, "user.getPic");

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("retriving new id for user");
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                keys.next();
                System.out.println("Полученный ключ: "+keys.next());
                user.setId(keys.getInt(1));
            }

//            if (rs.next()) {
//                System.out.println("rs int " + rs.getInt(1));
//                user.setId(rs.getInt(1));
//            }
            //обнуляем пароль
            System.out.println("Record is inserted into CB_USER table!");

        } catch (SQLException e) {
            System.out.println("User NOT added, cause - "+e.getMessage());
            return null;
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

        //Установка ролей для нового пользователя
        ArrayList<UserRole> userRoles = new ArrayList<>();
        UserRole uRole = new UserRole();
        uRole.setId(3);
        userRoles.add(uRole);
        userRoles = rC.setAndCreateUserRole(userRoles, user);
        user.setUserRoles(userRoles);

        return user;

    }
//------

    public boolean login(User user) {
        return hg.checkHash(user.getPass(), user.getHash());
    }

//------

    /**
     * Метод возвращает список пользователей формата ID - NAME 
     * используется на странице администратора
     * @return список пользователей из базы для страницы администратора
     * @throws SQLException
     */
    public ArrayList<User> getAllUsers() throws SQLException {
        User user;
        ArrayList<User> userList = new ArrayList<>();
        connection=dataSource.getConnection();
//        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        preparedStatement = null;
        String query = "SELECT USERID, USERNAME FROM CB_USER ORDER BY USERNAME";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("USERID"));
                user.setName(rs.getString("USERNAME"));
                userList.add(user);
            }
        } catch (Exception e) {
            System.out.println("getAllUsers " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return userList;
    }
//------
    public ArrayList<User> getUsersByRole(Integer roleId){
        User user;
        ArrayList<User> userList = new ArrayList<>();
        PreparedStatement statement;
        try {
            connection=dataSource.getConnection();
//            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            String query = "SELECT USERID, USERNAME "
                    + "FROM CB_USER "
                    + "INNER JOIN CB_USERROLE "
                    + "ON CB_USERROLE.UR_USERID = CB_USER.USERID "
                    + "WHERE CB_USERROLE.UR_ROLEID = ? "
                    + "ORDER BY USERNAME";
            statement = connection.prepareStatement(query);
            statement.setInt(1, roleId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("USERID"));
                user.setName(rs.getString("USERNAME"));
                userList.add(user);
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("БИН ВЫДАЛ: "+ex.getMessage());
        }
        return userList;
    }
//------

    /**
     * Поиск пользователя в базе по имени
     * @param userName
     * @return
     */
    public User findUser(String userName) {
        User user = null;
        String query = "SELECT * FROM CB_USER WHERE USERNAME = ?";
        try {
            connection=dataSource.getConnection();
//            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = null;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("USERID"));
                user.setName(rs.getString("USERNAME"));
                user.setEmail(rs.getString("USERMAIL"));
                user.setHash(rs.getString("USERPASSHASH"));
                user.setNote(rs.getString("USERNOTES"));
                user.setPicPath(rs.getString("USERPIC"));
            }
            user.setUserRoles(rC.getUserRoles(user));
        } catch (Exception e) {
            System.out.println("findUser by name: " + e.getMessage());
            return null;
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
        return user;
    }
//------
    /**
     * Поиск юзера в базе по ID
     * @param userId
     * @return
     * @throws SQLException
     */
    public User findUser(Integer userId) throws SQLException {
        User user = null;
        connection=dataSource.getConnection();
//        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement statement = null;
        String query = "SELECT * FROM CB_USER WHERE USERID = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("USERID"));
                user.setName(rs.getString("USERNAME"));
                user.setEmail(rs.getString("USERMAIL"));
                user.setHash(rs.getString("USERPASSHASH"));
                user.setNote(rs.getString("USERNOTES"));
                user.setPicPath(rs.getString("USERPIC"));
            }
            user.setUserRoles(rC.getUserRoles(user));
        } catch (Exception e) {
            System.out.println("findUser by id " + e.getMessage());
            return null;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
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
        return user;
    }
//------    

    /**
     * Обновление данных пользователя
     * Используется на странице администратора
     * 
     * @param userId
     * @param column поле в БД которое требует апдейта
     * @param value новое значение поля 
     * @throws SQLException
     */
    public void updateUserData(Integer userId, String column, String value) throws SQLException 
    {
        connection=dataSource.getConnection();
//        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        Statement statement = connection.createStatement();
        String query = "UPDATE CB_USER"
                + " SET " + column + "='" + value + "'"
                + " WHERE USERID=" + userId;
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("UpdateUserData - " + query + e);
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
//------    
    /**
     * Включение-отключение роли пользователя
     *
     * @param userId
     * @param roleId 
     * @param value может быть false и true. Но строковое. Если true то добавляем роль, если false то удаляем
     * @throws SQLException
     */
    public void updateUserRole(Integer userId, Integer roleId, String value) throws SQLException 
    {
        connection=dataSource.getConnection();
//        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        Statement statement = connection.createStatement();
        String query;
        if("false".equals(value))
        {
            query = "DELETE FROM CB_USERROLE"
                    + " WHERE "
                    + "UR_ROLEID="+roleId
                    + " AND "
                    + "UR_USERID=" + userId;
        }
        else
        {
            query = "INSERT INTO CB_USERROLE (UR_USERID, UR_ROLEID) "
                    + "VALUES ("
                    + userId+", "
                    + roleId
                    + ")";
        }
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("UpdateUserRole - " + query + e);
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
