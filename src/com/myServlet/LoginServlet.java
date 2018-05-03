package com.myServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.positionWebApp.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private String username;
    private String password;
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public void init(){
        // Do required initialization
    }




    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String s;
        boolean b = false;

//        System.out.println("Arrivata post");

        Reader r=request.getReader();
        Scanner scanner=new Scanner(r);
        s=scanner.nextLine();
        Account account = mapper.readValue(s, Account.class);

        String username = account.getUsername();
        String password = account.getPassword();

        System.out.println("Arrivata post su login " + username + " " + password);

        if (username == null || password == null || username.isEmpty() || password.isEmpty() ) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}