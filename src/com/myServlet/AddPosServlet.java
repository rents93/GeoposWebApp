package com.myServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static java.lang.Double.parseDouble;

@WebServlet("/positions")
public class AddPosServlet extends HttpServlet {
    private double latD;
    private double lonD;
    private long tsL;
    private String latS;
    private String lonS;
    private String tsS;

    protected static UserPosMap tab=new UserPosMap();

    @Override
    public void init(){
        // Do required initialization
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        latS=request.getParameter("latitude");
        lonS=request.getParameter("longitude");

        if(latS==null || lonS==null){
            //manca un valore
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty field");
        }
        else{
            //creo oggetto posizione
            latD=parseDouble(latS);
            lonD=parseDouble(lonS);
            tsL=System.currentTimeMillis();
            Position p = new Position(latD,lonD,tsL);

            //valido coordinate
            if(p.hasValidCoord()){
                //coordinate corrette

                //controllo se esiste user e aggiungo la posizione, se valida
                String currUser=request.getSession().getAttribute("username").toString();

                if(tab.userIsPresent(currUser)){
                    //confronto con ultima posizione
//                    if(p.isValidPos(tab.getLastPos(currUser))) {
//                        //posizione valida
                        tab.addPos(currUser, p);
//                    }
//
//                    else
//                        //posizione non valida rispetto a quella precedente
//                        redirectMessage(request, response, "<font color=red>Posizione non valida rispetto a quella precedente</font>");
                }
                else{
                    //utente da aggiungere alla lista
                    tab.addUser(currUser);
                    tab.addPos(currUser, p);
                }
            }
            else{
                //coordinate non valide
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid coordinates");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //da interfaccia web
        String n_pos = req.getParameter("n_pos");
        String user = req.getSession().getAttribute("username").toString();
        PrintWriter out = resp.getWriter();
        out.println("Richieste " + n_pos + " posizioni di " + user + " dimensione tab relativa ");

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