package sample;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

public class Popup {

    public static Stage popup(String text) {
        StackPane stackPane = new StackPane(new Text(text));
        Scene popupScene = new Scene(stackPane, 75, 75);
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false); // prevents resize and removes minimize and maximize buttons
        return popupStage;
    }

    public static JFileChooser inputSelect(String title,int selectionModel) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setFileSelectionMode(selectionModel);
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

}
