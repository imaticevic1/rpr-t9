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
        Controller con = new Controller();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("geografijaDAO.fxml"));
        primaryStage.setTitle("Drzave i gradovi");
        loader.setController(con);
        primaryStage.setScene(new Scene(loader.load(), 600, 400));
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
