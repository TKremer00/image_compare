package views;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import restoreGui.DetailsGui;
import restoreGui.InputGui;
import sample.FileHandler;
import sample.Popup;


class RestoreGui extends GridPane {


    private InputGui inputGui;
    private DetailsGui detailsGui;

    private FileHandler fileHandler;

    RestoreGui() {
        GridPane p = this;
        fileHandler = new FileHandler();

        inputGui = new InputGui();
        inputGui.setPrefHeight(110);

        detailsGui = new DetailsGui();
        detailsGui.setPrefHeight(70);

        p.add(inputGui,0,0);

        p.add(detailsGui,0,1);

        Button btnRun = new Button("Run");
        p.add(btnRun,0,2);

        setHalignment(btnRun, HPos.RIGHT);

        btnRun.setOnAction(event -> {
            String message;

            if(inputGui.getDirString().getText().length() > 0) {
                fileHandler.restoreImages(inputGui.getDirString().getText(),detailsGui.getSelectedItem());
                message = "Restored the images";
            }else {
                message = "No directory specified";
            }

            Popup.popup(message);
        });

    }
}
