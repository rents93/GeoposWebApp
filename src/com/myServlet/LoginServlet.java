package com.myServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.positionWebApp.Account;
import com.positionWebApp.DBQuery;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private String username;
    private String password;
    private ObjectMapper mapper = new ObjectMapper();
    private DbFunction dbFunction = DbFunction.getDbFunction();

    @Override
    public void init(){
        // Do required initialization
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String s, query;
        boolean b = false;
        ResultSet rs=null;

//        System.out.println("Arrivata post");

        Reader r=request.getReader();
        Scanner scanner=new Scanner(r);
        s=scanner.nextLine();
        Account account = mapper.readValue(s, Account.class);

        String username = account.getUsername();
        String password = account.getPassword();

        System.out.println("Arrivata post su login " + username + " " + password);

        if (username.isEmpty() || password.isEmpty() ) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
//            //controllo se le credenziali sono valide
//            String searchStr = username + ":" + password;
//
//            //apertura file
//            Scanner scan = new Scanner(new File(
//                    request.getServletContext().getRealPath("WEB-INF/users.txt"))
//            );
//            //ricerca credenziali nel file
//            while (scan.hasNext()) {
//                s = scan.nextLine();
//                if (s.equals(searchStr)) {
//                    //Credenziali corrette
//                    request.getSession().setAttribute("username", username);
//                    b = true;
//                }
//            }
//
//            ///occhio al nome tabella---------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            query="select count (*) as num from USER where username='"+username+"'and password='"+password+"'";
//            rs=DBQuery.doQuery(query);
//
//            try {
//                if(rs.getInt("num")==1){
//                    b=true;
//                }
//            } catch (SQLException e) {
//                System.out.println("Errore query ricerca user nel db");
//            }
            dbFunction.checkCredentials(username, password);


            if (!b)
                //credenziali non corrispondono
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}