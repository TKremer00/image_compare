package views;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.swing.*;

public class DirInput extends GridPane {

    private TextField dirString;
    private RadioButton rbDeleteDoubles;

    public DirInput() {
        this.setHgap(10);

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Open directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);


        dirString = new TextField();
        dirString.setMinSize(230,10);
        dirString.setEditable(false);
        this.add(dirString,0,0,4,1);


        Button btnOpen = new Button("Open dir");
        this.add(btnOpen,0,1);

        rbDeleteDoubles = new RadioButton("Delete doubles");
        this.add(rbDeleteDoubles,0,2);

        btnOpen.setOnAction(event -> {
            if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                dirString.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });
    }

    public String getDirString() {
        return dirString.getText();
    }

    public boolean getRbDelete() {
        return rbDeleteDoubles.isSelected();
    }
}
