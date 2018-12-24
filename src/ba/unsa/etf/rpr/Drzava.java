package ba.unsa.etf.rpr;

public class Drzava {
    String naziv;
    String glavni_grad;

    public Drzava(String naziv, String glavni_grad) {
        this.naziv = naziv;
        this.glavni_grad = glavni_grad;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getGlavni_grad() {
        return glavni_grad;
    }

    public void setGlavni_grad(String glavni_grad) {
        this.glavni_grad = glavni_grad;
    }
}
