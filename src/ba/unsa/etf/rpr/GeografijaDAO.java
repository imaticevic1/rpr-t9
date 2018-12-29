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
        String[] naziviGradova = new String[]{"London", "Pariz", "Beč", "Manchester", "Graz"};
        String[] naziviDrzava = new String[]{"Velika Britanija", "Francuska", "Austrija", "Velika Britanija", "Austrija"};
        Integer[] stanovnici = new Integer[]{8825000, 2206488, 1899055, 545500, 280200};
        instance.isprazniTablice();
        for(int i = 0; i  < stanovnici.length; i++) {
            Grad g = new Grad();
            Drzava d = new Drzava();
            g.setNaziv(naziviGradova[i]);
            g.setBrojStanovnika(stanovnici[i]);
            d.setNaziv(naziviDrzava[i]);
            if(i < 3)
                d.setGlavniGrad(g);
            g.setDrzava(d);
            instance.dodajGrad(g);
            instance.dodajDrzavu(d);

        }
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
            deleteGrad = conn.prepareStatement("DELETE FROM grad WHERE drzava=?");//brise se kad i drzava u kojoj se nalazi
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
        Drzava drz = new Drzava();
        drz.setNaziv(drzava);
        try {
        selectDrzava.setString(1, drzava);
        ResultSet rs = selectDrzava.executeQuery();
        selectGrad.setInt(1, rs.getInt("glavni_grad"));
        rs = selectGrad.executeQuery();
        if(rs.next()) {
            glavni.setNaziv(rs.getString("naziv"));
            glavni.setBrojStanovnika(rs.getInt("broj_stanovnika"));
            glavni.setDrzava(drz);
            glavni.getDrzava().setGlavniGrad(glavni);
            return glavni;
        }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
   public void obrisiDrzavu(String drzava){
       try {
           selectDrzava.setString(1, drzava);
           ResultSet rs = selectDrzava.executeQuery();// dat ce id glavnog grada ako ima drzave u tablici
           while(rs.next()) {
               deleteGrad.setInt(1, rs.getInt("glavni_grad"));
               deleteGrad.executeUpdate();
           }
           selectDrzava.setString(1, drzava);
           rs = selectDrzava.executeQuery();
           if(rs.next()) {
               deleteDrzava.setString(1, drzava);
               deleteDrzava.executeUpdate();
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
            ResultSet rs = s.executeQuery("SELECT * FROM GRAD");
            ResultSet rs1;
            while(rs.next()){
                Grad g = new Grad();
                Drzava d = new Drzava();
                //System.out.println(rs.getInt(1) + rs.getString(2) + rs.getInt(3) + rs.getInt(4));
                g.setNaziv(rs.getString(2));
                g.setBrojStanovnika(rs.getInt(3));
                rs1 = s2.executeQuery("SELECT naziv FROM DRZAVA WHERE id=" + rs.getInt("drzava"));
                if(rs1.next()) {
                    d = nadjiDrzavu(rs1.getString("naziv"));
                    g.setDrzava(d);
                    listaGradova.add(g);
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        listaGradova.sort((Grad g1, Grad g2) -> {
            if(g1.getBrojStanovnika() > g2.getBrojStanovnika()) return -1;
            else if(g1.getBrojStanovnika() < g2.getBrojStanovnika()) return 1;
            return 0;
        });
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
                rsId = s.executeQuery("SELECT ID FROM GRAD WHERE broj_stanovnika=" + grad.getBrojStanovnika());
            }

            Statement s1 = conn.createStatement();
            ResultSet rs;
            if(grad.getDrzava().getGlavniGrad() != null) {
                rs = s1.executeQuery("SELECT id FROM DRZAVA WHERE NAZIV='" + grad.getDrzava().getNaziv() + "'");
                if (rs.next() && grad.getDrzava().getGlavniGrad().getNaziv().equals(grad.getNaziv())) {
                    updateDrzava.setInt(1, rsId.getInt(1));
                    updateDrzava.setString(2, grad.getDrzava().getNaziv());
                    updateDrzava.executeUpdate();
                }
            }
                rs = s1.executeQuery("SELECT id FROM DRZAVA WHERE NAZIV='" + grad.getDrzava().getNaziv() + "'");
                if(rs.next()) {
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
            ResultSet rsId = s.executeQuery("SELECT ID FROM drzava WHERE naziv='" + drzava.getNaziv() + "'");
            if(!rsId.next()) {
                insertDrzava.setString(2, drzava.getNaziv());
                insertDrzava.executeUpdate();
                rsId = s.executeQuery("SELECT ID FROM drzava WHERE naziv='" + drzava.getNaziv() + "'");
            }
            Statement s1 = conn.createStatement();
            if(drzava.getGlavniGrad() != null) {
                ResultSet rs = s1.executeQuery("SELECT * FROM grad WHERE naziv='" + drzava.getGlavniGrad().getNaziv() + "'");
                if (rs.next()) {
                    updateDrzava.setInt(1, rs.getInt(1));
                    updateDrzava.setString(2, drzava.getNaziv());
                    updateDrzava.executeUpdate();
                    updateGrad.setInt(1, rsId.getInt(1));
                    updateGrad.setInt(2, rs.getInt(3));
                    updateGrad.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void izmijeniGrad(Grad grad){
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM GRAD WHERE broj_stanovnika=" + grad.getBrojStanovnika());
            if(rs.next()){
                s.executeUpdate("UPDATE grad SET naziv='" + grad.getNaziv() + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Drzava nadjiDrzavu(String drzava){
        Grad glavni = this.glavniGrad(drzava);
        if(glavni == null) return null;
        Drzava d = new Drzava();
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM DRZAVA WHERE naziv='" + drzava + "'");
                d.setNaziv(drzava);
                d.setGlavniGrad(glavni);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return d;
    }
    public void isprazniTablice(){
        try {
            Statement s = conn.createStatement();
            Statement s2 = conn.createStatement();
            s.executeUpdate("DELETE FROM GRAD");
            s.executeUpdate("DELETE FROM DRZAVA");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
