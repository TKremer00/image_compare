package compareGui;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import sample.CheckMethods;
import sample.Popup;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class DirInputGui extends Pane {

    private TextField dirString;
    private RadioButton rbDeleteDoubles;
    private ToggleGroup group;
    private int size;

    public DirInputGui() {
        Pane p = this;
        group = new ToggleGroup();
        List<CheckMethods> methods = Arrays.asList(CheckMethods.values());
        JFileChooser chooser = Popup.inputSelect("Open directory",JFileChooser.DIRECTORIES_ONLY);

        Text titleInput = new Text("Directory input");
        titleInput.setX(10);
        titleInput.setY(10);

        Line lineInput = new Line();
        lineInput.setStartX(10);
        lineInput.setStartY(25);
        lineInput.setEndX(240);
        lineInput.setEndY(25);

        dirString = new TextField();
        dirString.setMinSize(220,10);
        dirString.setEditable(false);
        dirString.setTranslateX(20);
        dirString.setTranslateY(35);

        Button btnOpen = new Button("Open dir");
        btnOpen.setTranslateX(20);
        btnOpen.setTranslateY(65);

        rbDeleteDoubles = new RadioButton("Delete doubles");
        rbDeleteDoubles.setTranslateX(20);
        rbDeleteDoubles.setTranslateY(95);

        int start = 115;
        for (int i = 0; i < methods.size(); i++) {
            RadioButton rb = new RadioButton(methods.get(i).getName());
            if(i == 0){
                rb.setSelected(true);
            }
            rb.setToggleGroup(group);
            rb.setUserData(methods.get(i).getNumber());
            rb.setTranslateX(20);
            rb.setTranslateY(start);
            start += 20 + (20 * i);
            p.getChildren().add(rb);
        }
        size = start;
        p.getChildren().addAll(titleInput,lineInput,dirString,btnOpen,rbDeleteDoubles);

        btnOpen.setOnAction(event -> {
            if (chooser.showDialog(Popup.getDialog(),"Select") == JFileChooser.APPROVE_OPTION) {
                dirString.setText(chooser.getSelectedFile().getAbsoluteFile().getAbsolutePath());
            }
        });
    }

    public String getDirString() {
        return dirString.getText() + "\\";
    }

    public boolean getRbDelete() {
        return rbDeleteDoubles.isSelected();
    }

    public int getMethod() {  return (int)group.getSelectedToggle().getUserData(); }

    public int getSize() { return size; }
}
