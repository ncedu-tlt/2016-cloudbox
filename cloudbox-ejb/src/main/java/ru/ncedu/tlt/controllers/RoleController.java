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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.management.relation.Role;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.entity.UserRole;
import ru.ncedu.tlt.properties.PropertiesCB;

/**
 *
 * @author Andrew
 */
@Stateless
@LocalBean
public class RoleController {

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    public RoleController() {
    }

    /**
     *
     * @param userRoles
     * @param user
     * @return
     */
    public ArrayList<UserRole> setAndCreateUserRole(ArrayList<UserRole> userRoles, User user) {
        //Проверка на пустоту
        if (userRoles == null) {
            return new ArrayList<>();
        }
        if (userRoles.isEmpty()) {
            return userRoles;
        }

        String insertTableSQL = "INSERT INTO CB_USERROLE (UR_USERID, UR_ROLEID) VALUES (?,?)";

//        for (int i = 0; i < userRoles.size() - 1; i++) {
//            insertTableSQL.append(", (?,?)");
//        }
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);
            preparedStatement = connection.prepareStatement(insertTableSQL);

//            connection.setAutoCommit(false);
            for (UserRole role : userRoles) {
                System.out.println("role is " + role.toString());
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setInt(2, role.getId());
                preparedStatement.execute();
//                preparedStatement.addBatch();
            }

//            preparedStatement.executeBatch();
//            connection.commit();
//            connection.setAutoCommit(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
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

        return userRoles;
    }

   public ArrayList<UserRole> getUserRoles(User user) {
        ArrayList<UserRole> roles = new ArrayList<>();
        UserRole role = null;

        String query = "SELECT * FROM CB_USERROLE WHERE UR_USERID = ?";
        try {
            connection = DriverManager.getConnection(PropertiesCB.CB_JDBC_URL);

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                role = new UserRole();
                role.setId(rs.getInt("UR_ROLEID"));
                role.setName("");
                roles.add(role);

            }
        } catch (Exception e) {
            System.out.println("findUser " + e.getMessage());
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

        return roles;
    }

}
