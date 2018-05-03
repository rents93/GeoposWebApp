package com.myServlet;

import com.positionWebApp.Position;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.List;

public class DbFunction {

    //TODO LkF ha detto di non mettere i dati hardcoded ma prenderli da un file di configurazione
    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "postgres";
    private final String password = "password";

    private static DbFunction dbfunction = new DbFunction();

    public static DbFunction getDbFunction(){
        return dbfunction;
    }


    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    private DbFunction close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public long insertPosition(Position p,String userID){
        PreparedStatement ps = null;
        String SQL = "INSERT INTO position(lat,lon,timestamp,userID) "
                + "VALUES(?,?,?,?)";

        long id = 0;

        try {
            Connection conn = this.connect();
            ps = conn.prepareStatement(SQL,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, String.valueOf(p.getLatitude()));
            ps.setString(2, String.valueOf(p.getLongitude()));
            ps.setString(3, String.valueOf(p.getTimestamp()));
            ps.setString(4, userID);

            int affectedRows = ps.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    id = rs.getLong(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            try {
                if (ps!=null)
                    ps.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return id;
    }

    public boolean checkCredentials(String username, String password){
        PreparedStatement ps = null;
        String SQL = "SELECT COUNT (*) AS NUM FROM USER WHERE USERNAME=? AND PASSWORD=?";
        try {
            Connection conn = this.connect();
            ps = conn.prepareStatement(SQL);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.getInt("num") == 1)
                //lo username_password esiste
                return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (ps!=null)
                    ps.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public List<Position> getPositions(String user, int n_pos) {

        String n= String.valueOf(n_pos);
        String SQL = "SELECT * FROM POSITION WHERE userID='"+user+"' limit "+n;

        try{
            Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL);
            // display actor information

                List<Position> lista=new ArrayList<>();
                while(rs.next()) {
                    Position p = new Position();
                    p.setLatitude(rs.getDouble("lat"));
                    p.setLongitude(rs.getDouble("lon"));
                    p.setTimestamp(rs.getLong("timestamp"));
                    lista.add(p);}

                    return lista;

            }catch(SQLException ex){
                throw new RuntimeException(ex);

            }

    }
}
