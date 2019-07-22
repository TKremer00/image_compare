package views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Gui {

    private TabPane tp;
    private RestoreGui restoreGui;
    private CompareGui compareGui;

    public Gui(Pane p) {
        tp = new TabPane();
        tp.setTabMinWidth(33);
        tp.setTabMinHeight(33);
        tp.setTabMaxWidth(69);
        tp.setTabMaxHeight(69);

        compareGui = new CompareGui();
        restoreGui = new RestoreGui();

        Tab compare = new Tab("Compare");
        compare.setClosable(false);
        compare.setContent(compareGui);

        Tab restore = new Tab("Restore");
        restore.setClosable(false);
        restore.setContent(restoreGui);


        tp.getTabs().add(compare);
        tp.getTabs().add(restore);

        p.getChildren().add(tp);
    }
}
