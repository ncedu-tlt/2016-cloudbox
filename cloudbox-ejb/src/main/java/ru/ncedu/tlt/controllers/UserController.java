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
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.entity.UserRole;
import ru.ncedu.tlt.hash.HashGenerator;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author Andrew
 */
@Singleton
@LocalBean
public class UserController {

    @EJB
    private RoleController rC;

    @EJB
    HashGenerator hg;

    Connection connection;

    public UserController() {

    }

    public User createUser(User user) {
        user.setHash(hg.getHash(user.getPass()));
        user.setPass("");
        System.out.println("getted userhash");

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO CB_USER"
                + "(USERMAIL, USERPASSHASH, USERSALT, USERNAME, USERNOTES, USERPIC) VALUES"
                + "(?,?,?,?,?,?)";

        int key = 0;
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
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
                
                user.setId(keys.getInt(1));
            }

//            if (rs.next()) {
//                System.out.println("rs int " + rs.getInt(1));
//                user.setId(rs.getInt(1));
//            }
            //обнуляем пароль
            System.out.println("Record is inserted into CB_USER table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    public ArrayList<User> getAllUsers() throws SQLException {
        User user = null;
        ArrayList<User> userList = new ArrayList<>();
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        String query = "SELECT USERID, USERNAME FROM CB_USER";
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

    public User findUser(String userName) throws SQLException {
        User user = null;
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM CB_USER WHERE USERNAME = ?";
        try {
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
        } catch (Exception e) {
            System.out.println("findUser " + e.getMessage());
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        
        user.setUserRoles(rC.getUserRoles(user));
        
        return user;
    }

//------
    public User findUser(Integer userId) throws SQLException {
        User user = null;
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM CB_USER WHERE USERID = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
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

        } catch (Exception e) {
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return user;
    }
//------    

    public void updateUserData(Integer userId, String column, String value) throws SQLException {
        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
        Statement statement = connection.createStatement();
        String query = "UPDATE CB_USER"
                + " SET " + column + "='" + value + "'"
                + " WHERE USERID=" + userId;
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("UpdateUser - " + query + e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
