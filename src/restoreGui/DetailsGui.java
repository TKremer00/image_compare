package restoreGui;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class DetailsGui extends Pane {

    private ComboBox cbExtensions;

    public DetailsGui() {
        Pane p = this;

        Text titleExtensions = new Text("DetailsGui");
        titleExtensions.setX(10);
        titleExtensions.setY(10);

        Line lineExtensions = new Line();
        lineExtensions.setStartX(10);
        lineExtensions.setStartY(25);
        lineExtensions.setEndX(240);
        lineExtensions.setEndY(25);

        cbExtensions = new ComboBox();
        cbExtensions.setItems(FXCollections.observableArrayList("png","jpg"));
        cbExtensions.getSelectionModel().select(0);
        cbExtensions.setTranslateX(20);
        cbExtensions.setTranslateY(40);

        p.getChildren().addAll(titleExtensions,lineExtensions,cbExtensions);
    }

    public String getSelectedItem() {
        return cbExtensions.getSelectionModel().getSelectedItem().toString();
    }
}
