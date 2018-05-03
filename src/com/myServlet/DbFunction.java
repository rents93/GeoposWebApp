package com.myServlet;

import com.positionWebApp.Position;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class DbFunction {

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
        //Connection conn = null;
        //PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        //ResultSet rs = null;

        String SQL = "INSERT INTO position(lat,lon,timestamp,userID) "
                + "VALUES(?,?,?,?)";

        long id = 0;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            Date date = new Date();
            Timestamp ts=new Timestamp(date.getTime());

            pstmt.setString(1, String.valueOf(p.getLatitude()));
            pstmt.setString(2, String.valueOf(p.getLongitude()));
            pstmt.setString(3, String.valueOf(p.getTimestamp()));
            pstmt.setString(4, userID);


            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;

    }
}
