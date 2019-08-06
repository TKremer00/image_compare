package compareGui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import sample.Popup;

import javax.swing.*;

public class FileInputGui extends Pane {

    private TextField file1string,file2string;

    public FileInputGui() {
        Pane p = this;

        JFileChooser chooser = Popup.inputSelect("Open File",JFileChooser.FILES_ONLY);

        Text titleInput = new Text("File input");
        titleInput.setX(10);
        titleInput.setY(10);

        Line lineInput = new Line();
        lineInput.setStartX(10);
        lineInput.setStartY(25);
        lineInput.setEndX(240);
        lineInput.setEndY(25);

        file1string = new TextField();
        file1string.setMinSize(220,10);
        file1string.setEditable(false);
        file1string.setTranslateX(20);
        file1string.setTranslateY(35);

        Button btnOpen = new Button("Open file");
        btnOpen.setTranslateX(20);
        btnOpen.setTranslateY(65);

        file2string = new TextField();
        file2string.setMinSize(220,10);
        file2string.setTranslateX(20);
        file2string.setTranslateY(95);


        Button btnOpen2 = new Button("Open file");
        btnOpen2.setTranslateX(20);
        btnOpen2.setTranslateY(125);

        p.getChildren().addAll(titleInput,lineInput,file1string,btnOpen,file2string,btnOpen2);

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
