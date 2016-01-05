/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.*;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.RoleController;
import ru.ncedu.tlt.controllers.UserController;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.entity.UserRole;

/**
 *
 * @author pavel.tretyakov
 */
@WebServlet(name = "UserControllerServlet", urlPatterns = {"/userProcess/*"})
public class UserControllerServlet extends HttpServlet {

    @EJB
    UserController userController;
    @EJB
    RoleController roleController;    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
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
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                User user;
                Gson gsonObject = new Gson();
                try {
                    user = userController.findUser(userId);
                    String json = gsonObject.toJson(user);
                    rs.print(json);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "getAllRoles":
            {
                Gson gsonObject = new Gson();
                try {
                    ArrayList<UserRole> allRolesList = (ArrayList) roleController.getAllUserRoles();
                    String json = gsonObject.toJson(allRolesList);
                    System.out.println(json);
                    rs.print(json);
                } catch (SQLException ex) {
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
            break;
            }
            case "updateUserRole":
            {
            try {
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                Integer roleId = Integer.valueOf(request.getParameter("roleId"));
                String value = request.getParameter("is");
                userController.updateUserRole(userId, roleId, value);
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
