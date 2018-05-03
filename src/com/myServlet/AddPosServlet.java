package com.myServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.positionWebApp.Account;
import com.positionWebApp.Position;
import com.positionWebApp.UserPosMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@WebServlet("/positions")
public class AddPosServlet extends HttpServlet {

    private static ObjectMapper mapper = new ObjectMapper();
    protected static UserPosMap tab = UserPosMap.getUserPosMap();

    @Override
    public void init(){
        // Do required initialization
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {


        System.out.println("Arrivata post su positions");
        List<Position> positions = new ArrayList<>();
        DbFunction db= new DbFunction();

        Reader r=request.getReader();
        Scanner scanner=new Scanner(r);
        String s;
        while (scanner.hasNextLine()){
            s=scanner.nextLine();
            try {
                positions = Arrays.asList(mapper.readValue(s, Position[].class));
            } catch (Exception e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong format input, unable to parse json");
            }
        }

        for (Position p : positions){
            //valido coordinate
            if(p.hasValidCoord()){
                String currUser=request.getSession().getAttribute("username").toString();
                if(tab.userIsPresent(currUser)){
                    //confronto con ultima posizione
                    if(p.isValidPos(tab.getLastPos(currUser)))
                        db.insertPosition(p,currUser);
                       // tab.addPos(currUser, p);
                    else
                        ;
//                        non faccio nulla, semplicemente non viene inserita
//                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "At least one positions doesn't respect velocity constraint");
                } else {
                    //utente da aggiungere alla lista
                    tab.addUser(currUser);
                    tab.addPos(currUser, p);
                }
            } else
                ;
//                coordinate non valide
//                non faccio nulla, semplicemente non viene inserita
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid coordinates");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //da interfaccia web
        String n_pos = req.getParameter("n_pos");
        String user = req.getSession().getAttribute("username").toString();
        PrintWriter out = resp.getWriter();
//        out.println("Richieste " + n_pos + " posizioni di " + user + " dimensione tab relativa ");

        List<Position> lista = tab.getPositions(user, Integer.parseInt(n_pos) );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), lista);
        resp.setContentType("application/json");
    }

    private void redirectMessage(HttpServletRequest request, HttpServletResponse response, String s)
            throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        out.println(s);
        RequestDispatcher rd=request.getRequestDispatcher("/positions.html");
        rd.include(request, response);
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}