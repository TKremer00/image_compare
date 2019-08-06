package restoreGui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import sample.Popup;

import javax.swing.*;

public class InputGui extends Pane {

    private TextField dirString;

    public InputGui() {
        Pane p = this;
        JFileChooser chooser = Popup.inputSelect("Open directory", JFileChooser.DIRECTORIES_ONLY);

        Text text = new Text("Input");
        text.setX(10);
        text.setY(15);

        Line line = new Line();
        line.setStartX(10);
        line.setStartY(30);
        line.setEndX(240);
        line.setEndY(30);

        dirString = new TextField();
        dirString.setMinSize(220,10);
        dirString.setEditable(false);
        dirString.setTranslateX(20);
        dirString.setTranslateY(40);

        Button btnOpen = new Button("Open dir");
        btnOpen.setTranslateX(178);
        btnOpen.setTranslateY(70);

        p.getChildren().addAll(text,line,dirString,btnOpen);

        btnOpen.setOnAction(event -> {
            if (chooser.showDialog(Popup.getDialog(),"Select") == JFileChooser.APPROVE_OPTION) {
                dirString.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });
    }

    public TextField getDirString() {
        return dirString;
    }
}
