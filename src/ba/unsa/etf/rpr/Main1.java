package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main1 extends Application {
    @Override
    //
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        Controller con = new Controller();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/geografijaDAO.fxml"), bundle);
        primaryStage.setTitle(bundle.getString("Drzaveigradovi"));
        loader.setController(con);
        primaryStage.setScene(new Scene(loader.load(), 600, 400));
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
