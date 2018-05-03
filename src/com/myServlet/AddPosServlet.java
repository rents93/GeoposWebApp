package com.myServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.positionWebApp.DBQuery;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

        String username=request.getSession().getAttribute("username").toString();

        //ricavo id user corrente nella tabella USER
        String query="select userID as id from USER where username='"+username+"'";
        ResultSet rs=DBQuery.doQuery(query);
        int userID=-1, result=-1;
        Position prec=null;
        try {
            userID=rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Errore get id user");
        }
        if(userID!=-1) {

            for (Position p : positions) {
                //valido coordinate
                if (p.hasValidCoord()) {
                    query = "select *" +
                            "from POSITION" +
                            "where userID='" + userID + "'" +
                            "and timestamp= (" +
                                "select max(timestamp)" +
                                "from POSITION" +
                                "where userID='" + userID +"')";

                    rs = DBQuery.doQuery(query);
                    try {
                        prec=new Position(rs.getDouble("lat"), rs.getDouble("lon"),
                                rs.getLong("timestamp"));
                    } catch (SQLException e) {
                        System.out.println("Errore ricerca punto precedente");
                    }
                    if(p.isValidPos(prec)){
                        //inserire nuovo punto nel db
                        query = "insert into POSITION (posID, lat, lon, timestamp, userID)" +
                                "values (..... , '"+p.getLatitude()+"', '"+p.getLongitude()+"', '"+p.getTimestamp()+"'," +
                                "'"+userID+"')";

                        result = DBQuery.doUpdate(query);

                    }
                } else {
                    //Posizione non inserita nel db
                    System.out.println("Posizione non valida");
                }


            }
        }

        /*for (Position p : positions){
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
        }*/
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //da interfaccia web
        DbFunction db= new DbFunction();
        int n_pos = Integer.parseInt(req.getParameter("n_pos"));
        String user = req.getSession().getAttribute("username").toString();
        PrintWriter out = resp.getWriter();
//        out.println("Richieste " + n_pos + " posizioni di " + user + " dimensione tab relativa ");

        List<Position> lista=db.getPositions(user,n_pos);

        ObjectMapper mapper=new ObjectMapper();
        mapper.writeValue(resp.getWriter(),lista);
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