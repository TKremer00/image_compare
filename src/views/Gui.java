package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Gui {

    private final ToggleGroup selectInput;
    private FileInput fileInput;
    private DirInput dirInput;
    private ObservableList<String> extentions;

    public Gui(Pane p) {
        selectInput = new ToggleGroup();

        fileInput = new FileInput();
        fileInput.setTranslateX(20);
        fileInput.setTranslateY(100);
        p.getChildren().add(fileInput);

        dirInput = new DirInput();
        dirInput.setTranslateX(20);
        dirInput.setTranslateY(100);

        extentions = FXCollections.observableArrayList("png","jpg");

        Text text = new Text("Input method");
        text.setX(10);
        text.setY(15);
        p.getChildren().add(text);

        Line line = new Line();
        line.setStartX(10);
        line.setStartY(30);
        line.setEndX(250);
        line.setEndY(30);
        p.getChildren().add(line);

        RadioButton rbFile = new RadioButton("File");
        rbFile.setToggleGroup(selectInput);
        rbFile.setTranslateX(20);
        rbFile.setTranslateY(40);
        rbFile.setSelected(true);
        p.getChildren().add(rbFile);

        RadioButton rbDir = new RadioButton("Directory");
        rbDir.setToggleGroup(selectInput);
        rbDir.setTranslateX(110);
        rbDir.setTranslateY(40);
        p.getChildren().add(rbDir);

        Text titleInput = new Text("File input");
        titleInput.setX(10);
        titleInput.setY(75);
        p.getChildren().add(titleInput);

        Line lineInput = new Line();
        lineInput.setStartX(10);
        lineInput.setStartY(90);
        lineInput.setEndX(250);
        lineInput.setEndY(90);
        p.getChildren().add(lineInput);

        Text titleExtention = new Text("File input");
        titleExtention.setX(10);
        titleExtention.setY(170);
        p.getChildren().add(titleExtention);

        Line lineExtention = new Line();
        lineExtention.setStartX(10);
        lineExtention.setStartY(185);
        lineExtention.setEndX(250);
        lineExtention.setEndY(185);
        p.getChildren().add(lineExtention);


        ComboBox cbExtention = new ComboBox();
        cbExtention.setItems(extentions);
        cbExtention.setTranslateX(20);
        cbExtention.setTranslateY(195);
        p.getChildren().add(cbExtention);

        //Event listener Toggle group
        selectInput.selectedToggleProperty().addListener(event -> {
            if(rbFile.isSelected()) {
                titleInput.setText("File input");
                p.getChildren().remove(dirInput);
                p.getChildren().add(fileInput);
            }else if(rbDir.isSelected()){
                titleInput.setText("Directory input");
                p.getChildren().remove(fileInput);
                p.getChildren().add(dirInput);
            }
        });

        Button btnRun = new Button("Run");
        btnRun.setTranslateX(215);
        btnRun.setTranslateY(210);
        p.getChildren().add(btnRun);
    }
}
