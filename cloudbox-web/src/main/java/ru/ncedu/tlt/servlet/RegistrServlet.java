/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
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
import ru.ncedu.tlt.filter.FilterParam;
import ru.ncedu.tlt.filter.FilterSettingsXML;

/**
 *
 * @author Andrew
 */
@WebServlet(name = "RegistrServlet", urlPatterns = {"/registr"})
public class RegistrServlet extends HttpServlet {

    @EJB
    UserController ucEjb;

    @EJB
    FilterSettingsXML filtSettings;

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
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {

//        String userNameNew = request.getParameter("regUserName");
        String userNameNew = new String(request.getParameter("regUserName").getBytes(
                "iso-8859-1"), "UTF-8");
//        String userPassNew = request.getParameter("regUserPass");
        String userPassNew = new String(request.getParameter("regUserPass").getBytes(
                "iso-8859-1"), "UTF-8");

//         String userPassNew2 = request.getParameter("regUserPass2");
        String userPassNew2 = new String(request.getParameter("regUserPass2").getBytes(
                "iso-8859-1"), "UTF-8");

//        String userEmailNew = request.getParameter("regEmail");
        String userEmailNew = new String(request.getParameter("regEmail").getBytes(
                "iso-8859-1"), "UTF-8");

        if (userNameNew == null || userPassNew == null || userPassNew2 == null || userEmailNew == null || userNameNew.length() == 0 || userPassNew.length() == 0 || userPassNew2.length() == 0 || userEmailNew.length() == 0) {
            request.setAttribute("message", "Заполните все поля");

            request.getRequestDispatcher(FilterParam.REGISTR_JSP).forward(request, response);
            return;
//            response.sendRedirect(FilterParam.REGISTR_JSP);
        }

        if (!userPassNew.equals(userPassNew2) || userPassNew.length() == 0) {
            request.setAttribute("message", "Пароли не совпадают");
            request.getRequestDispatcher(FilterParam.REGISTR_JSP).forward(request, response);
            return;
//            response.sendRedirect(FilterParam.REGISTR_JSP);
        }

        if (ucEjb.findUser(userNameNew) != null) {
            request.setAttribute("message", "Пользователь " + userNameNew + " уже существует");
            request.getRequestDispatcher(FilterParam.REGISTR_JSP).forward(request, response);
            return;
//            response.sendRedirect(FilterParam.REGISTR_JSP);
        } else {
            User user = new User();

            user.setName(userNameNew);
            user.setPass(userPassNew);
            user.setEmail(userEmailNew);

            user = ucEjb.createUser(user);
            if (user == null) {
                request.setAttribute("message", "Не получилось создать пользователя " + userNameNew);
                request.getRequestDispatcher(FilterParam.REGISTR_JSP).forward(request, response);
                return;
//                response.sendRedirect(FilterParam.REGISTR_JSP);
            }

            System.out.println("New user " + user.toString());
            request.getSession().setAttribute("userName", user.getName());
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("logged", true);
            request.getSession().setAttribute("userroles", user.rolesToString());
            response.sendRedirect(filtSettings.getFirstAllowebPage(user));
        }
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(RegistrServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegistrServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RegistrServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegistrServlet.class.getName()).log(Level.SEVERE, null, ex);
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
