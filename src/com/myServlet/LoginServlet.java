package com.myServlet;

import com.positionWebApp.UserPosMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private String username;
    private String password;

    @Override
    public void init(){
        // Do required initialization
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String s;
        boolean b = false;
        username = request.getParameter("username");
        password = request.getParameter("password");

        System.out.println("Arrivata post su login" + username + password);

        if (username == null || password == null || username.isEmpty() || password.isEmpty() ) {
            response.setStatus(401);
        } else {
            //controllo se le credenziali sono valide
            String searchStr = username + ":" + password;
            Scanner scan = new Scanner(new File(
                    request.getServletContext().getRealPath("WEB-INF/users.txt"))
            );
            while (scan.hasNext()) {
                s = scan.nextLine();
                if (s.equals(searchStr)) {
                    //Credenziali corrette
                    request.getSession().setAttribute("username", username);
//                    da scommentare per interfaccia web
//                    ritora automaticamente 200 al client
//                    response.sendRedirect("positions.html");
                    b = true;
                }
            }
            if (!b)
                response.setStatus(401);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}