package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:baza.db";
       // String upit = "CREATE TABLE GRAD, ID PRIMARY KEY, naziv";

        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            Class.forName("org.sqlite.JDBC");
           // ResultSet result = stmt.executeQuery(upit);

        }
        catch (SQLException e ){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            //    e.printStackTrace();
        }
    }
}
