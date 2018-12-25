package ba.unsa.etf.rpr;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GeografijaDAO geo = GeografijaDAO.getInstance();
       /*String[] naziviGradova = new String[]{"London", "Pariz", "Beč", "Manchester", "Graz"};
        String[] naziviDrzava = new String[]{"Engleska", "Francuska", "Austrija", "Engleska", "Austrija"};
        Integer[] stanovnici = new Integer[]{8787892, 2200000, 1868000, 550000, 846000};
        geo.isprazniTablice();
     for(int i = 0; i  < stanovnici.length; i++) {
            Grad g = new Grad();
            Drzava d = new Drzava();
            g.setNaziv(naziviGradova[i]);
            g.setBrojStanovnika(stanovnici[i]);
            d.setNaziv(naziviDrzava[i]);
            if(i < 3)
            d.setGlavniGrad(g);
            g.setDrzava(d);
         geo.dodajGrad(g);
            geo.dodajDrzavu(d);

            }*/
        //System.out.println(g.getDrzava().getNaziv() + " " + d.getGlavniGrad().getDrzava().getGlavniGrad().getNaziv());
        //System.out.println("Gradovi su:\n" + ispisiGradove());
        //glavniGrad();
        geo.gradovi();
    }
}
