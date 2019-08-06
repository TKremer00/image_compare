package views;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Gui {

    private TabPane tp;
    private RestoreGui restoreGui;
    private CompareGui compareGui;

    public Gui(Pane p) {

        compareGui = new CompareGui();
        restoreGui = new RestoreGui();

        Tab compare = new Tab("Compare");
        compare.setClosable(false);
        compare.setContent(compareGui);

        Tab restore = new Tab("Restore");
        restore.setClosable(false);
        restore.setContent(restoreGui);

        tp = new TabPane();
        tp.setTabMinWidth(33);
        tp.setTabMinHeight(33);
        tp.setTabMaxWidth(69);
        tp.setTabMaxHeight(69);
        tp.getTabs().add(compare);
        tp.getTabs().add(restore);
        tp.setPrefWidth(250);
        compare.getTabPane().setPrefHeight(500);
        restore.getTabPane().setPrefHeight(500);

        p.getChildren().add(tp);
    }
}
