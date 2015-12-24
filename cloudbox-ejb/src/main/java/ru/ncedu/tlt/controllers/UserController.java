/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.controllers;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import ru.ncedu.tlt.entity.User;
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

        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getHash());
            preparedStatement.setString(3, "user.getSalt");
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, "user.getNotes");
            preparedStatement.setString(6, "user.getPic");

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("retriving new id for user");
//            ResultSet rs = preparedStatement.getGeneratedKeys();
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

        return user;

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public User findUser(String userName) throws SQLException {
        User user = null;

        connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);

        PreparedStatement preparedStatement = null;

        String query = "SELECT * FROM CB_USER WHERE USERNAME = ?";

        
        System.out.println("trying to find user by name " +  userName);
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

        return user;

    }

    public boolean login(User user) {
        return hg.checkHash(user.getPass(), user.getHash());

    }
}
