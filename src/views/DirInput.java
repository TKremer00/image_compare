package views;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DirInput extends GridPane {

    public DirInput() {
        this.setHgap(5);

        TextField file1string = new TextField();
        this.add(file1string,0,0);

        Button btnOpen = new Button("Open dir");
        this.add(btnOpen,1,0);

        RadioButton rbDeleteDoubles = new RadioButton("Delete doubles");
        this.add(rbDeleteDoubles,0,1);
    }
}
