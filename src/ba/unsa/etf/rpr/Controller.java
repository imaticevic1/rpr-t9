package ba.unsa.etf.rpr;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements Runnable{
    public Hyperlink meniOpcija;
    public Hyperlink meniOpcija1;
    public Hyperlink meniOpcija2;
    public Hyperlink meniOpcija3;
    public Hyperlink meniOpcija4;
    public Hyperlink meniOpcija5;
    public GridPane gridPane1;
    public GridPane gridPane2;
    public GridPane gridPane3;
    public GridPane gridPane4;
    public GridPane gridPane5;
    public TextField drzavaTekst;
    public TextField gradTekst;
    public TextField stanovniciTekst;
    public Button dugmeDodaj;
    public Button glGrad;
    public Button dugmeBrisi;
    public TextField drzava1Tekst;
    public TextField drzava2Tekst;
    private boolean ispravanGrad = false;
    private boolean ispravnaDrzava = false;
    private boolean ispravnaDrzava1 = false;
    private boolean ispravnaDrzava2 = false;
    private boolean ispravniStanovnici = false;
    private HashMap<Integer, GridPane> mapa = new HashMap<>();
    private GeografijaDAO geo;
    private SimpleStringProperty glavni = new SimpleStringProperty("");
    private SimpleStringProperty brisanje = new SimpleStringProperty("");
    private SimpleStringProperty spisak = new SimpleStringProperty("");
    private SimpleStringProperty drzava = new SimpleStringProperty("");
    private SimpleStringProperty grad = new SimpleStringProperty("");
    private SimpleStringProperty stanovnici = new SimpleStringProperty("");
    private SimpleStringProperty dugmeLabela = new SimpleStringProperty("Klikni me da saznaš glavni grad!");

    public Controller(){
        geo = GeografijaDAO.getInstance();
        }
public void napraviDrzavu(String nazivGrada, String nazivDrzave, Integer brojStan){
    Grad g = new Grad();
    Drzava d = new Drzava();
    g.setNaziv(nazivGrada);
    g.setBrojStanovnika(brojStan);
    d.setNaziv(nazivDrzave);
    d.setGlavniGrad(g);
    g.setDrzava(d);
    geo.dodajGrad(g);
    geo.dodajDrzavu(d);

}
private void omoguci(int broj){
       Thread thread1 = new Thread(new Runnable() {
           @Override
           public void run() {
               for(int  i = 1; i < 6; i++)
                   if (i == broj) mapa.get(i).setVisible(true);
                   else mapa.get(i).setVisible(false);
           }
       });
       thread1.start();
}
    public boolean isSlovo(String s){
        int brojRazmaka = 0;
        boolean imaRazlicit = false;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == ' '){
                brojRazmaka++;
                if(brojRazmaka == 1)continue;
            }
            if(s.charAt(i) != ' '){
                brojRazmaka = 0;
                imaRazlicit = true;
            }
            if (Character.isDigit(s.charAt(i))) return false;
        }
        if(!imaRazlicit) return false;
        return true;

    }
    public boolean isDigit(String s){
        if(s.equals("") || s == null) return false;
        for(int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i))) return false;
        return true;
    }
    public void initialize(){
        mapa.put(1, gridPane1);
        mapa.put(2, gridPane2);
        mapa.put(3, gridPane3);
        mapa.put(4, gridPane4);
        mapa.put(5, gridPane5);
        meniOpcija.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                omoguci(1);
            }
        });
        /*meniOpcija1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                omoguci(2);
                setGrad("Naziv grada");
                setDrzava("Naziv države u kojoj se nalazi grad");
                setStanovnici("Broj stanovnika grada");

                drzavaTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == "" || newValue == null || !isSlovo(newValue)){
                            drzavaTekst.getStyleClass().removeAll("poljeIspravno");
                            drzavaTekst.getStyleClass().add("poljeNeispravno");
                            ispravnaDrzava = false;
                        }
                        else{
                            drzavaTekst.getStyleClass().removeAll("poljeNeispravno");
                            drzavaTekst.getStyleClass().add("poljeIspravno");
                            ispravnaDrzava = true;
                        }
                    }
                });
                gradTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == " " || newValue == null || !isSlovo(newValue)){
                            gradTekst.getStyleClass().removeAll("poljeIspravno");
                            gradTekst.getStyleClass().add("poljeNeispravno");
                            ispravanGrad = false;
                        }
                        else{
                            gradTekst.getStyleClass().removeAll("poljeNeispravno");
                            gradTekst.getStyleClass().add("poljeIspravno");
                            ispravanGrad = true;
                        }
                    }
                });
                stanovniciTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == " " || newValue == null || !isDigit(newValue)){
                            stanovniciTekst.getStyleClass().removeAll("poljeIspravno");
                            stanovniciTekst.getStyleClass().add("poljeNeispravno");
                            ispravniStanovnici = false;
                        }
                        else{
                            stanovniciTekst.getStyleClass().removeAll("poljeNeispravno");
                            stanovniciTekst.getStyleClass().add("poljeIspravno");
                            ispravniStanovnici = true;
                        }
                    }
                });
                if(ispravanGrad && ispravnaDrzava && ispravniStanovnici)
                    napraviGrad(gradTekst.getText(), drzavaTekst.getText(), Integer.parseInt(stanovniciTekst.getText()));
            }
        });*/
        meniOpcija2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gradTekst.setText("");
                stanovniciTekst.setText("");
                drzavaTekst.setText("");
                omoguci(2);
                setGrad("Naziv glavnog grada");
                setDrzava("Naziv države");
                setStanovnici("Broj stanovnika glavnog grada");
                drzavaTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == "" || newValue == null || !isSlovo(newValue)){
                            drzavaTekst.getStyleClass().removeAll("poljeIspravno");
                            drzavaTekst.getStyleClass().add("poljeNeispravno");
                            ispravnaDrzava = false;
                        }
                        else{
                            drzavaTekst.getStyleClass().removeAll("poljeNeispravno");
                            drzavaTekst.getStyleClass().add("poljeIspravno");
                            ispravnaDrzava = true;
                        }
                    }
                });
                gradTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == " " || newValue == null || !isSlovo(newValue)){
                            gradTekst.getStyleClass().removeAll("poljeIspravno");
                            gradTekst.getStyleClass().add("poljeNeispravno");
                            ispravanGrad = false;
                        }
                        else{
                            gradTekst.getStyleClass().removeAll("poljeNeispravno");
                            gradTekst.getStyleClass().add("poljeIspravno");
                            ispravanGrad = true;
                        }
                    }
                });
                stanovniciTekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == " " || newValue == null || !isDigit(newValue)){
                            stanovniciTekst.getStyleClass().removeAll("poljeIspravno");
                            stanovniciTekst.getStyleClass().add("poljeNeispravno");
                            ispravniStanovnici = false;
                        }
                        else{
                            stanovniciTekst.getStyleClass().removeAll("poljeNeispravno");
                            stanovniciTekst.getStyleClass().add("poljeIspravno");
                            ispravniStanovnici = true;
                        }
                    }
                });
                dugmeDodaj.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(ispravanGrad && ispravnaDrzava && ispravniStanovnici)
                            napraviDrzavu(gradTekst.getText(), drzavaTekst.getText(), Integer.parseInt(stanovniciTekst.getText()));
                    }
                });

            }
        });
        meniOpcija3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                omoguci(3);
                drzava1Tekst.setText("");
                setDugmeLabela("Klikni me da saznaš glavni grad");
                setGlavni("");
                drzava1Tekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == "" || newValue == null || !isSlovo(newValue)){
                            drzava1Tekst.getStyleClass().removeAll("poljeIspravno");
                            drzava1Tekst.getStyleClass().add("poljeNeispravno");
                            ispravnaDrzava1 = false;
                        }
                        else{
                            drzava1Tekst.getStyleClass().removeAll("poljeNeispravno");
                            drzava1Tekst.getStyleClass().add("poljeIspravno");
                            ispravnaDrzava1 = true;
                        }
                    }
                });
                glGrad.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(ispravnaDrzava1)
                            if(geo.glavniGrad(drzava1Tekst.getText()) != null) {
                                setDugmeLabela("Glavni grad je " + geo.glavniGrad(drzava1Tekst.getText()).getNaziv());
                                setGlavni("Glavni grad države " + drzava1Tekst.getText() + " je " +
                                        geo.glavniGrad(drzava1Tekst.getText()).getNaziv());
                            }
                            else setDugmeLabela("Nema države u bazi!");
                    }
                });

            }
        });
        meniOpcija4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                omoguci(4);
                drzava2Tekst.setText("");
                drzava2Tekst.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == "" || newValue == null || !isSlovo(newValue)){
                            drzava2Tekst.getStyleClass().removeAll("poljeIspravno");
                            drzava2Tekst.getStyleClass().add("poljeNeispravno");
                            ispravnaDrzava2 = false;
                        }
                        else{
                            drzava2Tekst.getStyleClass().removeAll("poljeNeispravno");
                            drzava2Tekst.getStyleClass().add("poljeIspravno");
                            ispravnaDrzava2 = true;
                        }
                    }
                });
                dugmeBrisi.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(ispravnaDrzava2)
                            geo.obrisiDrzavu(drzava2Tekst.getText());
                    }
                });

            }
        });
        meniOpcija5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                omoguci(5);
                spisak.set(Main.ispisiGradove());
            }
        });
    }
    public void run(){

    }

    public String getBrisanje() {
        return brisanje.get();
    }

    public SimpleStringProperty brisanjeProperty() {
        return brisanje;
    }

    public void setBrisanje(String brisanje) {
        this.brisanje.set(brisanje);
    }

    public String getGlavni() {
        return glavni.get();
    }

    public SimpleStringProperty glavniProperty() {
        return glavni;
    }

    public void setGlavni(String glavni) {
        this.glavni.set(glavni);
    }

    public String getSpisak() {
        return spisak.get();
    }

    public SimpleStringProperty spisakProperty() {
        return spisak;
    }

    public void setSpisak(String spisak) {
        this.spisak.set(spisak);
    }

    public String getDrzava() {
        return drzava.get();
    }

    public SimpleStringProperty drzavaProperty() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava.set(drzava);
    }

    public String getGrad() {
        return grad.get();
    }

    public SimpleStringProperty gradProperty() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad.set(grad);
    }

    public String getStanovnici() {
        return stanovnici.get();
    }

    public SimpleStringProperty stanovniciProperty() {
        return stanovnici;
    }

    public void setStanovnici(String stanovnici) {
        this.stanovnici.set(stanovnici);
    }

    public String getDugmeLabela() {
        return dugmeLabela.get();
    }

    public SimpleStringProperty dugmeLabelaProperty() {
        return dugmeLabela;
    }

    public void setDugmeLabela(String dugmeLabela) {
        this.dugmeLabela.set(dugmeLabela);
    }
}
