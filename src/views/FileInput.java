package views;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javafx.scene.layout.GridPane;

public class FileInput extends GridPane {

    private TextField file1string,file2string;

    public FileInput() {
        this.setHgap(10);

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        file1string = new TextField();
        file1string.setMinSize(220,10);
        file1string.setEditable(false);
        this.add(file1string,0,0,4,1);

        Button btnOpen = new Button("Open file");
        this.add(btnOpen,0,1);

        file2string = new TextField();
        file2string.setMinSize(220,10);
        file2string.setEditable(false);
        this.add(file2string,0,2,4,1);

        Button btnOpen2 = new Button("Open file");
        this.add(btnOpen2,0,3);



        btnOpen.setOnAction(event -> {
            chooser.setDialogTitle("Open image 1");
            if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                file1string.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });

        btnOpen2.setOnAction(event -> {
            chooser.setDialogTitle("Open image 2");
            if (chooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                file2string.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });
    }

    public String getFile1String() {
        return file1string.getText();
    }

    public String getFile2String() {
        return file2string.getText();
    }
}
