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
        executeCommand(request, response);
    }

    protected void executeCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String userManageRequest = request.getRequestURI().split("/")[request.getRequestURI().split("/").length - 1];
        response.setContentType("text/html");
        PrintWriter rs = response.getWriter();
        switch (userManageRequest) {
            case "getAllUsers": {
                Gson gson = new Gson();
                try {
                    String json = gson.toJson(userController.getAllUsers());
                    rs.print(json);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "getUserData": {
                Integer userId = Integer.valueOf(request.getParameter("userId"));
                User user;
                Gson gson = new Gson();
                try {
                    user = userController.findUser(userId);
                    rs.print(gson.toJson(user));
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "getUserLoggedData": {
                Integer userId = (Integer) request.getSession().getAttribute("userId");
                User user;
                Gson gson = new Gson();
                try {
                    user = userController.findUser(userId);
                    rs.print(gson.toJson(user));
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "getAllRoles": {
                Gson gson = new Gson();
                try {
                    ArrayList<UserRole> allRolesList = (ArrayList) roleController.getAllUserRoles();
                    String json = gson.toJson(allRolesList);
                    rs.print(json);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                break;
            }
            case "updateUserData": {
                try {
                    Integer userId = Integer.valueOf(request.getParameter("userId"));
                    String column = request.getParameter("column");
                    String value = request.getParameter("value");
                    userController.updateUserData(userId, column, value);
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);;
                }
                break;
            }
            case "updateUserRole": {
                try {
                    Integer userId = Integer.valueOf(request.getParameter("userId"));
                    Integer roleId = Integer.valueOf(request.getParameter("roleId"));
                    String value = request.getParameter("is");
                    userController.updateUserRole(userId, roleId, value);
                    break;
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            case "getUsersByRole": {
                Integer roleId = Integer.valueOf(request.getParameter("roleId"));
                Gson gson = new Gson();
                try {
                    String json = gson.toJson(userController.getUsersByRole(roleId));
                    rs.print(json);
                } catch (SQLException ex) {
                    System.out.println("ПРИ ВЫБОРЕ ЮЗЕРОВ ПО РОЛЯМ ВЫЛЕЗЛО: "+ex.getMessage());
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        executeCommand(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
