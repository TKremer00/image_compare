package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root, 300, 275));

        new Gui(root);

        primaryStage.setTitle("CompareImages");
        primaryStage.show();



        new FileHandler("F:\\Java images test\\").deleteSameImages();

    }

    public static void main(String[] args) {
        launch(args);
    }
}