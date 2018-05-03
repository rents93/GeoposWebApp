package com.positionWebApp;

import java.sql.*;

public class DBQuery {
    static private Connection conn;

//    LkF viene fatto nel listener
//    static public void driverInit() throws ClassNotFoundException{
//        Class.forName("com.mysql.jdbc.Driver");
//    }

    static public void connect() throws SQLException{
        String DBUrl = "jdbc:postgresql:database";
        conn=DriverManager.getConnection(DBUrl,"anton", "gruppo8");
    }

    static public ResultSet doQuery(String str){
        Statement st=null;
        ResultSet rs=null;
        try {
            st=conn.createStatement();
            rs=st.executeQuery(str);
        }
        catch (Exception e){System.out.println("Fallita la query");}
        finally{
            try{
                if(st!=null){
                    st.close();
                }
            }
            catch (Exception e){System.out.println("Fallita close() statement");}
        }

        return rs;
    }

    static public int doUpdate(String str){
        Statement st=null;
        int rs=-1;
        try {
            st=conn.createStatement();
            rs=st.executeUpdate(str);
        }
        catch (Exception e){System.out.println("Fallita l'update");}
        finally{
            try{
                if(st!=null){
                    st.close();
                }
            }
            catch (Exception e){System.out.println("Fallita close() statement");}
        }

        return rs; //ritorna il numero di righe modificate nel db
    }

}
