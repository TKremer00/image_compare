package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.FileHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RestoreGui extends Pane {


    private TextField dirString;
    private FileHandler fileHandler;

    public RestoreGui() {
        fileHandler = new FileHandler();
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Open directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        ObservableList<String> extensions = FXCollections.observableArrayList("png","jpg");

        Text text = new Text("Input");
        text.setX(10);
        text.setY(15);
        this.getChildren().add(text);

        Line line = new Line();
        line.setStartX(10);
        line.setStartY(30);
        line.setEndX(240);
        line.setEndY(30);
        this.getChildren().add(line);


        dirString = new TextField();
        dirString.setMinSize(220,10);
        dirString.setEditable(false);
        dirString.setTranslateX(20);
        dirString.setTranslateY(40);
        this.getChildren().add(dirString);

        Button btnOpen = new Button("Open dir");
        btnOpen.setTranslateX(178);
        btnOpen.setTranslateY(70);
        this.getChildren().add(btnOpen);


        Text titleExtensions = new Text("DetailsGui");
        titleExtensions.setX(10);
        titleExtensions.setY(110);
        this.getChildren().add(titleExtensions);

        Line lineExtensions = new Line();
        lineExtensions.setStartX(10);
        lineExtensions.setStartY(125);
        lineExtensions.setEndX(240);
        lineExtensions.setEndY(125);
        this.getChildren().add(lineExtensions);


        ComboBox cbExtensions = new ComboBox();
        cbExtensions.setItems(extensions);
        cbExtensions.getSelectionModel().select(0);
        cbExtensions.setTranslateX(20);
        cbExtensions.setTranslateY(140);
        this.getChildren().add(cbExtensions);

        Button btnRun = new Button("Run");
        btnRun.setTranslateX(203);
        btnRun.setTranslateY(170);
        this.getChildren().add(btnRun);


        btnOpen.setOnAction(event -> {
            if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                dirString.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });

        btnRun.setOnAction(event -> {
            StackPane stackPane = new StackPane(new Text("Something went wrong"));
            if(dirString.getText().length() > 0) {
                fileHandler.restoreImages(dirString.getText(),cbExtensions.getSelectionModel().getSelectedItem().toString());
                stackPane = new StackPane(new Text("Restored the images"));
            }else {
                stackPane = new StackPane(new Text("No directory specified"));
            }

            Scene popupScene = new Scene(stackPane, 75, 75);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(popupScene);
            popupStage.setResizable(false); // prevents resize and removes minimize and maximize buttons
            popupStage.show();
        });

    }
}
