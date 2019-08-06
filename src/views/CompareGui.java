package views;

import compareGui.DetailsGui;
import compareGui.DirInputGui;
import compareGui.FileInputGui;
import compareGui.InputMethodGui;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import sample.FileHandler;
import sample.Popup;

class CompareGui extends GridPane {


    private FileHandler fileHandler;

    private DirInputGui dirInputGui;
    private FileInputGui fileInputGui;
    private DetailsGui detailsGui;

    private InputMethodGui inputMethodGui;

    CompareGui() {
        GridPane p = this;
        p.setHgap(10);

        fileHandler = new FileHandler();

        inputMethodGui = new InputMethodGui();
        inputMethodGui.setPrefHeight(80);

        dirInputGui = new DirInputGui();
        dirInputGui.setPrefHeight(135);

        fileInputGui = new FileInputGui();
        fileInputGui.setPrefHeight(165);

        detailsGui = new DetailsGui();
        detailsGui.setPrefHeight(70);

        p.add(inputMethodGui,0,0);


        p.add(dirInputGui,0,1);

        p.add(detailsGui,0,2);

        Button btnRun = new Button("Run");
        p.add(btnRun,0,3);

        setHalignment(btnRun, HPos.RIGHT);

        //Event listener Toggle group*/
        inputMethodGui.getSelectInput().selectedToggleProperty().addListener(event -> {
            if(inputMethodGui.getRbDir().isSelected()){
                p.getChildren().remove(fileInputGui);
                p.add(dirInputGui,0,1);


            }else if(inputMethodGui.getRbFile().isSelected()) {
                p.getChildren().remove(dirInputGui);
                p.add(fileInputGui,0,1);
            }
        });

        btnRun.setOnAction(event -> {

            fileHandler.setFileExtension(detailsGui.getSelectedItem());
            String message = "Something went wrong";

            if(inputMethodGui.getRbFile().isSelected()) {
                message = (fileHandler.compare2Images(fileInputGui.getFile1String(),fileInputGui.getFile2String()) ? "Images are the same" : "Images aren't the same");

            }else if (inputMethodGui.getRbDir().isSelected()) {
                fileHandler.compareImages(dirInputGui.getDirString());
                message = "Done open txt to see";

                if(dirInputGui.getRbDelete()) {
                    message = (fileHandler.deleteSameImages(dirInputGui.getDirString()) ? "Double images removed" : "Deleting images failed");
                }
            }

            Popup.popup(message).show();
        });
    }
}
