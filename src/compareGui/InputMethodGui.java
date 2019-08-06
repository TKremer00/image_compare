package compareGui;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class InputMethodGui extends Pane {

    private final ToggleGroup selectInput;
    private final RadioButton rbDir,rbFile;

    public InputMethodGui() {
        Pane p = this;

        selectInput = new ToggleGroup();

        Text text = new Text("Input method");
        text.setX(10);
        text.setY(15);

        Line line = new Line();
        line.setStartX(10);
        line.setStartY(30);
        line.setEndX(240);
        line.setEndY(30);

        rbDir = new RadioButton("Directory");
        rbDir.setToggleGroup(selectInput);
        rbDir.setTranslateX(20);
        rbDir.setTranslateY(40);
        rbDir.setSelected(true);

        rbFile = new RadioButton("File");
        rbFile.setToggleGroup(selectInput);
        rbFile.setTranslateX(110);
        rbFile.setTranslateY(40);

        p.getChildren().addAll(text,line,rbDir,rbFile);
    }

    public RadioButton getRbDir() {
        return rbDir;
    }

    public RadioButton getRbFile() {
        return rbFile;
    }

    public ToggleGroup getSelectInput() {
        return selectInput;
    }
}
