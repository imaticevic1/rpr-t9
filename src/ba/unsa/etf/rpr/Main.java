package ba.unsa.etf.rpr;

import net.sf.jasperreports.engine.JRException;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String ispisiGradove(){
        GeografijaDAO geo = GeografijaDAO.getInstance();
        ArrayList<Grad> g = new ArrayList<Grad>();
        String s = "";
        g.addAll(geo.gradovi());
        for(Grad grad : g)
            s = s + grad.getNaziv() + " (" + grad.getDrzava().getNaziv() + ") - " + grad.getBrojStanovnika() + "\n";
        return s;
    }
    public static void glavniGrad(){
        GeografijaDAO geo = GeografijaDAO.getInstance();
        Scanner ulaz = new Scanner(System.in);
        String s = ulaz.nextLine();
        try {
            Grad g = geo.glavniGrad(s);
            System.out.println("Glavni grad države " + s + " je " + g.getNaziv());
        }catch(NullPointerException e){
            System.out.println("Drzava ne postoji u bazi!");
        }
    }
    public static void main(String[] args) {

    }
}
