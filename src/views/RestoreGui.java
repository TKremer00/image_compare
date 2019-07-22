package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.swing.*;

public class RestoreGui extends GridPane {


    private TextField dirString;

    public RestoreGui() {

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



        btnOpen.setOnAction(event -> {
            if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                dirString.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });

        ComboBox cbExtensions = new ComboBox();
        cbExtensions.setItems(extensions);
        cbExtensions.getSelectionModel().select(0);
        this.add(cbExtensions,0,2);

    }

    public String getDirString() {
        return dirString.getText();
    }


}
