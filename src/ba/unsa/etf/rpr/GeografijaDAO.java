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
    private PreparedStatement updateGrad;
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
            insertDrzava = conn.prepareStatement("INSERT INTO drzava (id, naziv) VALUES(?, ?)");
            deleteGrad = conn.prepareStatement("DELETE FROM grad WHERE id=?");//brise se kad i drzava u kojoj se nalazi
            deleteDrzava = conn.prepareStatement("DELETE FROM drzava WHERE naziv=?");
            selectGrad = conn.prepareStatement("SELECT * FROM grad WHERE id=?");
            selectDrzava = conn.prepareStatement("SELECT glavni_grad FROM drzava WHERE naziv=?");
            updateDrzava = conn.prepareStatement("UPDATE drzava SET glavni_grad=? WHERE naziv=?");
            updateGrad = conn.prepareStatement("UPDATE grad SET drzava=? WHERE broj_stanovnika=?");
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
        Grad glavni = new Grad();
        try {
        selectDrzava.setString(1, drzava);
        ResultSet rs = selectDrzava.executeQuery();
        selectGrad.setInt(1, rs.getInt("glavni_grad"));
        rs = selectGrad.executeQuery();
        if(rs.next()) {
            glavni.setNaziv(rs.getString("naziv"));
            glavni.setBrojStanovnika(rs.getInt("broj_stanovnika"));
        }
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
        ArrayList<Grad> listaGradova = new ArrayList<Grad>();

        try {
            Statement s = conn.createStatement();
            Statement s2 = conn.createStatement();
            //s.executeUpdate("DELETE FROM GRAD");
            //s.executeUpdate("DELETE FROM DRZAVA");
            ResultSet rs = s.executeQuery("SELECT * FROM GRAD");
            ResultSet rs1 = s2.executeQuery("SELECT * FROM DRZAVA");
            System.out.println();
            System.out.println();
            while (rs.next()){
                System.out.println(rs.getInt(1) + " " + " " + rs.getString(2) + " " +
                        rs.getInt(3) + " " + rs.getInt(4));
            }
            System.out.println();
            while (rs1.next()){
                System.out.println(rs1.getInt(1) + " " + " " + rs1.getString(2) + " " +
                        rs1.getInt(3));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaGradova;
    }
    // Najbolje je ubaciti u tablicu sve elemente pa tek na kraju foreign key
    // tako da bi bila neka vjerovatnoća da postoji grad ili drzava koji se dodaju
    public void dodajGrad(Grad grad){
        try {
            Statement s = conn.createStatement();
            ResultSet rsId = s.executeQuery("SELECT ID FROM GRAD WHERE broj_stanovnika=" + grad.getBrojStanovnika());
            if(!rsId.next()) {
                insertGrad.setString(2, grad.getNaziv());
                insertGrad.setInt(3, grad.getBrojStanovnika());
                insertGrad.executeUpdate();
            }
            else return;

            ResultSet rs = s.executeQuery("SELECT id FROM DRZAVA WHERE NAZIV='" + grad.getDrzava().getNaziv() + "'");
            if(rs.next()){
                updateDrzava.setInt(1, rsId.getInt(1));
                updateDrzava.setString(2, grad.getDrzava().getNaziv());
                updateDrzava.executeUpdate();
            }
            else if(rs.next()){
                updateGrad.setInt(1, rs.getInt(1));
                updateGrad.setInt(2, grad.getBrojStanovnika());
                updateGrad.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void dodajDrzavu(Drzava drzava){
        try {
            Statement s = conn.createStatement();
            ResultSet rsId = s.executeQuery("SELECT id FROM drzava WHERE naziv='" + drzava.getNaziv() + "'");
                insertDrzava.setString(2, drzava.getNaziv());
                insertDrzava.executeUpdate();

            ResultSet rs = s.executeQuery("SELECT * FROM grad WHERE naziv='" + drzava.getGlavniGrad().getNaziv() + "'");
            if(rs.next()){
                updateDrzava.setInt(1, rs.getInt(1));
                updateDrzava.setString(2, drzava.getNaziv());
                updateDrzava.executeUpdate();
            }
            else if(rs.next()){
                updateGrad.setInt(1, rsId.getInt(1));
                updateGrad.setInt(2, rs.getInt(3));
                updateGrad.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void izmijeniGrad(Grad grad){

    }
    public Drzava nadjiDrzavu(String drzava){
        return null;
    }
}
