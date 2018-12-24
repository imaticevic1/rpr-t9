package ba.unsa.etf.rpr;
import javafx.util.converter.PercentageStringConverter;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;
    private String gradTabela, drzavaTabela;
    private PreparedStatement insertGrad;
    private PreparedStatement deleteGrad;
    private PreparedStatement insertDrzava;
    private PreparedStatement deleteDrzava;
    private PreparedStatement selectDrzava;
    private PreparedStatement selectGrad;
    private PreparedStatement updateDrzava;
    private static void initialize(){
        instance = new GeografijaDAO();
    }
    private GeografijaDAO(){
        gradTabela = "CREATE TABLE IF NOT EXISTS grad (\n"
                + " id integer PRIMARY KEY,\n"
                + " naziv text NOT NULL,\n"
                + " broj_stanovnika text NOT NULL,\n"
                + " drzava integer,\n"
                + " FOREIGN KEY(drzava) REFERENCES drzava(id)\n"
                + ")";
        drzavaTabela = "CREATE TABLE IF NOT EXISTS drzava (\n"
                + " id integer PRIMARY KEY,\n"
                + " naziv text NOT NULL,\n"
                + "glavni_grad integer,\n"
                + "  FOREIGN KEY(glavni_grad) REFERENCES grad(id)\n"
                + ")";

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
            Statement s1 = conn.createStatement();
            s1.execute(gradTabela);
            s1.execute(drzavaTabela);
            insertGrad = conn.prepareStatement("INSERT INTO grad (id, naziv, broj_stanovnika, drzava) VALUES(?, ?, ?, ?)");
            insertDrzava = conn.prepareStatement("INSERT INTO drzava (id, naziv, glavni_grad) VALUES(?, ?, ?)");
            deleteGrad = conn.prepareStatement("DELETE FROM grad WHERE id=?");//brise se kad i drzava u kojoj se nalazi
            deleteDrzava = conn.prepareStatement("DELETE FROM drzava WHERE naziv=?");
            selectGrad = conn.prepareStatement("SELECT * FROM grad WHERE id=?");
            selectDrzava = conn.prepareStatement("SELECT glavni_grad FROM drzava WHERE naziv=?");
            updateDrzava = conn.prepareStatement("UPDATE drzava SET glavni_grad=? WHERE naziv=?");
           /* s1.executeUpdate("INSERT INTO GRAD(id, naziv, broj_stanovnika, drzava) VALUES(?,'VARES',7300, ?)");
            ResultSet rss = s1.executeQuery("SELECT * FROM GRAD");
            while(rss.next()){
                System.out.println(rss.getInt(1) + rss.getString(2) + rss.getInt(3) + rss.getInt(4));
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static GeografijaDAO getInstance(){
        if(instance == null) initialize();
        return instance;
    }
    public static void removeInstance(){
        instance = null;
    }
    public Grad glavniGrad(String drzava){
        try {
            selectDrzava.setString(1, drzava);
            ResultSet rs = selectDrzava.executeQuery();
            if(!rs.next()) return null;
            selectGrad.setInt(1, rs.getInt("glavni_grad"));
            rs = selectGrad.executeQuery();
            if(rs.next())
            return new Grad(rs.getString("naziv"), rs.getInt("broj_stanovnika"), drzava);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
   public void obrisiDrzavu(String drzava){
       try {
           selectDrzava.setString(1, drzava);
           ResultSet rs = selectDrzava.executeQuery();// dat ce id glavnog grada ako ima drzave u tablici
           if(rs.next()) {
               deleteDrzava.setString(1, drzava);
               deleteDrzava.executeUpdate();
               deleteGrad.setInt(1, rs.getInt("glavni_grad"));
               deleteGrad.executeUpdate();
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
    public ArrayList<Grad> gradovi(){
        return null;
    }
    public void dodajGrad(Grad grad){

    }
    public void dodajDrzavu(Drzava drzava){

    }
    public void izmijeniGrad(Grad grad){

    }
    public Drzava nadjiDrzavu(String drzava){
        return null;
    }
}
