/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.controllers.UserController;
import ru.ncedu.tlt.entity.User;
import ru.ncedu.tlt.hash.HashGenerator;

/**
 *
 * @author Andrey
 */
@WebServlet(name = "UpdateUserSettings", urlPatterns = {"/usersettings"})
public class UpdateUserSettings extends HttpServlet {

    @EJB
    UserController userController;

    @EJB
    HashGenerator hg;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String userEmail = new String(request.getParameter("regEmail").getBytes(
                "iso-8859-1"), "UTF-8");

        String userPass = new String(request.getParameter("regPass").getBytes(
                "iso-8859-1"), "UTF-8");

        Integer userId = (Integer) request.getSession().getAttribute("userId");

//        User user = userController.findUser(userId);
//        user.setEmail(userEmail);
//        user.setHash(hg.getHash(userPass));
        if (userEmail != null) {
            if (userEmail.length() != 0) {
                userController.updateUserData(userId, "USERMAIL", userEmail);
            }

        }

        if (userPass != null) {
            if (userPass.length() != 0) {
                userController.updateUserData(userId, "USERPASSHASH", hg.getHash(userPass));
            }

        }

        User user = userController.findUser(userId);
        request.setAttribute("usermail", user.getEmail());
        request.getRequestDispatcher("/usersettings.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            User user = userController.findUser(userId);
            request.setAttribute("usermail", user.getEmail());
            request.getRequestDispatcher("/usersettings.jsp").forward(request, response);

//            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateUserSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateUserSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
