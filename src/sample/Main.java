package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.Gui;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        Pane root = new Pane();
        primaryStage.setScene(new Scene(root, 300, 400));

        new Gui(root);

        primaryStage.setTitle("Compare images");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
