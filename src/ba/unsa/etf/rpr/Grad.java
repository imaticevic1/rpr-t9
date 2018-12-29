package ba.unsa.etf.rpr;

import javafx.beans.property.ObjectProperty;

public class Grad {
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava = new Drzava();
    public Grad() {
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava.setNaziv(drzava.getNaziv());
        if(drzava.getGlavniGrad() != null)
        this.drzava.setGlavniGrad(drzava.getGlavniGrad());
        else this.drzava.setGlavniGrad(null);
    }
}
