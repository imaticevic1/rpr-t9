package ba.unsa.etf.rpr;

import net.sf.jasperreports.engine.JRException;

public class GradoviReport {
    public void showReport(GeografijaDAO geo){
        try {
            new PrintReport().showReport(geo.getConn());
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
