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

                ArrayList<UserRole> allRolesList = (ArrayList) roleController.getAllUserRoles();
                ArrayList<UserRole> userRolesList = (ArrayList) user.getUserRoles();
                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                JsonObjectBuilder jOB = factory.createObjectBuilder();
                        jOB
                            .add("USERID", user.getId())
                            .add("USERNAME", user.getName())
                            .add("USERMAIL", user.getEmail())
                            .add("USERPASSHASH", user.getHash())
                            .add("USERNOTES", user.getNote())
                            .add("USERPIC", user.getPicPath());
                            JsonArrayBuilder roles = factory.createArrayBuilder();
                            UserRole role;
                            for(int i=0; i<allRolesList.size();i++)
                            {
                                role=allRolesList.get(i);
                                String c="";
                                for(int j=0; j<userRolesList.size();j++)
                                {
                                    if(userRolesList.get(j).getId() == allRolesList.get(i).getId())
                                    {
                                        c = "checked";
                                        break;
                                    }
                                }
                                roles.add(factory.createObjectBuilder()
                                            .add("ROLEID",role.getId())
                                            .add("ROLENAME",role.getName())
                                            .add("CHECKED",c)
                                );
                            }
                        jOB.add("ROLES",roles);
                        JsonObject jO = jOB.build();
                rs.print(jO);
                System.out.println(jO);
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
