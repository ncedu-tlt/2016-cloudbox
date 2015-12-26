/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.UserController;
import ru.ncedu.tlt.entity.User;

/**
 *
 * @author pavel.tretyakov
 */
@WebServlet(name = "UserControllerServlet", urlPatterns = {"/userProcess/*"})
public class UserControllerServlet extends HttpServlet {

    @EJB
    UserController userController;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userManageRequest = request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1];
        response.setContentType("text/html");
        PrintWriter rs = response.getWriter();
        switch (userManageRequest) 
        {
            case "getAllUsers": {
                ArrayList<User> userList;
                try {
                    userList = userController.getAllUsers();
                    JsonBuilderFactory factory = Json.createBuilderFactory(null);
                    JsonArrayBuilder jAB = factory.createArrayBuilder();
                    JsonArray jA;
                    for (User user : userList)
                    {
                        jAB.add(factory.createObjectBuilder()
                            .add("USERID", user.getId())
                            .add("USERNAME", user.getName())
                        );
                    }   
                    jA=jAB.build();
                    rs.print(jA);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "getUserData": 
            {
            try {
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                User user = userController.findUser(userId);
                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                JsonObject jO = factory.createObjectBuilder()
                            .add("USERID", user.getId())
                            .add("USERNAME", user.getName())
                            .add("USERMAIL", user.getEmail())
                            .add("USERPASSHASH", user.getHash())
                            .add("USERNOTES", user.getNote())
                            .add("USERPIC", user.getPicPath())
                            .build();
                rs.print(jO);
            } catch (SQLException ex) 
            {
                System.out.println(ex);
            }
            break;
            }
            case "updateUserData":
            {
            try {
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                String column = request.getParameter("column");
                String value = request.getParameter("value");
                userController.updateUserData(userId, column, value);
                break;
            } catch (SQLException ex) {
                Logger.getLogger(UserControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
