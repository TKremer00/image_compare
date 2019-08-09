package sample;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Popup {

    public static void popup(String text) {
        Platform.runLater(() -> {
            int width = 7 * text.length();
            StackPane stackPane = new StackPane(new Text(text));
            Scene popupScene = new Scene(stackPane, width, 50);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(popupScene);
            popupStage.setResizable(false); // prevents resize and removes minimize and maximize buttons
            popupStage.show();
        });
    }

    public static JFileChooser inputSelect(String title,int selectionModel) {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setFileSelectionMode(selectionModel);
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(false);

        return chooser;
    }

    public static JDialog getDialog() {
        JDialog dialog = new JDialog();
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);
        return dialog;
    }

}
