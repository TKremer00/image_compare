package views;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class FileInput extends GridPane {

    public FileInput() {
        this.setHgap(5);

        TextField file1string = new TextField();
        this.add(file1string,0,0);

        Button btnOpen = new Button("Open file");
        this.add(btnOpen,1,0);

        TextField file2string = new TextField();
        this.add(file2string,0,1);

        Button btnOpen2 = new Button("Open file");
        this.add(btnOpen2,1,1);
    }
}
