package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.FileHandler;

import javax.swing.*;

public class RestoreGui extends GridPane {


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

        dirString = new TextField();
        dirString.setMinSize(230,10);
        dirString.setEditable(false);
        this.add(dirString,0,0,4,1);


        Button btnOpen = new Button("Open dir");
        this.add(btnOpen,0,1);

        ComboBox cbExtensions = new ComboBox();
        cbExtensions.setItems(extensions);
        cbExtensions.getSelectionModel().select(0);
        this.add(cbExtensions,0,2);

        Button btnRun = new Button("Restore");
        this.add(btnRun,1,2);

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
