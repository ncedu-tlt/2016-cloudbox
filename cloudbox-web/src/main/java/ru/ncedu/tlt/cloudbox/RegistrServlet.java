/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ncedu.tlt.cloudbox;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.ncedu.tlt.contorllers.UserController;
import ru.ncedu.tlt.entity.User;

/**
 *
 * @author Andrew
 */
@WebServlet(name = "RegistrServlet", urlPatterns = {"/registr"})
public class RegistrServlet extends HttpServlet {

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
            throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String userId = request.getParameter("userId");

        if (userName != null && userId != null) {
            response.sendRedirect("drive.jsp");
        }


        String userNameNew = request.getParameter("regUserName");
        String userPassNew = request.getParameter("regUserPass");
        String userPassNew2 = request.getParameter("regUserPass");
        String userEmailNew = request.getParameter("regEmail");


        if (userNameNew == null || userPassNew == null || userPassNew2 == null || userEmailNew == null ||"".equals(userNameNew)||"".equals(userPassNew)||"".equals(userPassNew2)||"".equals(userEmailNew)) {
            request.getRequestDispatcher("registr.jsp").forward(request, response);
        }

        if (!userPassNew.equals(userPassNew2)) {
            request.getRequestDispatcher("registr.jsp").forward(request, response);
        }

        UserController uC = UserController.getInstance();

        if (uC.isUserExist(userName)) {
            request.getRequestDispatcher("registr.jsp").forward(request, response);
        } else {

            User user = uC.addUser(new User(0, userNameNew, userPassNew, userEmailNew));
            System.out.println("new user " + user.toString());
            request.getSession().setAttribute("userName", user.getName());
            request.getSession().setAttribute("userId", user.getId());
            response.sendRedirect("drive.jsp");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
