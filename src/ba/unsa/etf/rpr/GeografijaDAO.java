package ba.unsa.etf.rpr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;

public class GeografijaDAO {
    private static GeografijaDAO geografija = null;
    private Connection conn;
    private void initialize(){
        CREATE TABLE 
    geografija = new GeografijaDAO();
    }
    private GeografijaDAO(){

        String url = "jdbc:sqlite:baza.db";
        // String upit = "";

        try{
             conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            // ResultSet result = stmt.executeQuery(upit);
            Class.forName("org.sqlite.JDBC");
        }
        catch (SQLException e ){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            //    e.printStackTrace();
        }
    }
    public Grad glavniGrad(String drzava){
        return null;
    }
    public void obrisiDrzavu(String drzava){}
    ArrayList<Grad> gradovi(){
        return null;
    }
}
