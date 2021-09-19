package ch.bbcag.mdriver.views;

import ch.bbcag.mdriver.Constants;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ButtonBoxView extends HBox {
    public ButtonBoxView(Pane pane) {
        setAlignment(Pos.CENTER);
        prefWidthProperty().bind(pane.widthProperty());
        setPadding(Constants.BOTTOM_BOX_INSET);
    }
}
