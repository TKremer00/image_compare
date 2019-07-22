package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.FileHandler;

public class CompareGui extends Pane {

    private final ToggleGroup selectInput;
    private FileHandler fileHandler;
    private FileInput fileInput;
    private DirInput dirInput;

    public CompareGui() {
        Pane p = this;

        selectInput = new ToggleGroup();
        fileHandler = new FileHandler();

        dirInput = new DirInput();
        dirInput.setTranslateX(20);
        dirInput.setTranslateY(100);
        p.getChildren().add(dirInput);

        fileInput = new FileInput();
        fileInput.setTranslateX(20);
        fileInput.setTranslateY(100);

        ObservableList<String> extensions = FXCollections.observableArrayList("png","jpg");

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


        RadioButton rbDir = new RadioButton("Directory");
        rbDir.setToggleGroup(selectInput);
        rbDir.setTranslateX(20);
        rbDir.setTranslateY(40);
        rbDir.setSelected(true);
        p.getChildren().add(rbDir);

        RadioButton rbFile = new RadioButton("File");
        rbFile.setToggleGroup(selectInput);
        rbFile.setTranslateX(110);
        rbFile.setTranslateY(40);
        p.getChildren().add(rbFile);

        Text titleInput = new Text("Directory input");
        titleInput.setX(10);
        titleInput.setY(75);
        p.getChildren().add(titleInput);

        Line lineInput = new Line();
        lineInput.setStartX(10);
        lineInput.setStartY(90);
        lineInput.setEndX(250);
        lineInput.setEndY(90);
        p.getChildren().add(lineInput);

        Text titleExtensions = new Text("Details");
        titleExtensions.setX(10);
        titleExtensions.setY(230);
        p.getChildren().add(titleExtensions);

        Line lineExtensions = new Line();
        lineExtensions.setStartX(10);
        lineExtensions.setStartY(245);
        lineExtensions.setEndX(250);
        lineExtensions.setEndY(245);
        p.getChildren().add(lineExtensions);


        ComboBox cbExtensions = new ComboBox();
        cbExtensions.setItems(extensions);
        cbExtensions.getSelectionModel().select(0);
        cbExtensions.setTranslateX(20);
        cbExtensions.setTranslateY(255);
        p.getChildren().add(cbExtensions);

        Button btnRun = new Button("Run");
        btnRun.setTranslateX(215);
        btnRun.setTranslateY(280);
        p.getChildren().add(btnRun);

        //Event listener Toggle group
        selectInput.selectedToggleProperty().addListener(event -> {
            if(rbDir.isSelected()){
                titleInput.setText("Directory input");
                p.getChildren().remove(fileInput);
                p.getChildren().add(dirInput);
            }else if(rbFile.isSelected()) {
                titleInput.setText("File input");
                p.getChildren().remove(dirInput);
                p.getChildren().add(fileInput);
            }
        });

        btnRun.setOnAction(event -> {

            fileHandler.setFileExtension(cbExtensions.getSelectionModel().getSelectedItem().toString());

            StackPane stackPane = new StackPane(new Text("Something went wrong"));

            if(rbFile.isSelected()) {
                fileHandler.setInputFile1(fileInput.getFile1String());
                fileHandler.setInputFile2(fileInput.getFile2String());


                if(fileHandler.compare2Images()) {
                    stackPane = new StackPane(new Text("Images are the same"));

                }else {
                    stackPane = new StackPane(new Text("Images aren't the same"));
                }

            }else if (rbDir.isSelected()) {
                fileHandler.setInputDirectory(dirInput.getDirString());

                if(dirInput.getRbDelete()) {
                    fileHandler.deleteSameImages();
                    stackPane = new StackPane(new Text("Delete duplicates"));
                }else {
                    fileHandler.compareImages();
                    stackPane = new StackPane(new Text("Done open txt to see"));
                }
            }

            Scene popupScene = new Scene(stackPane, 75, 75);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(popupScene);
            popupStage.setResizable(false); // prevents resize and removes minimize and maximize buttons
            popupStage.show();
        });
    }
}
