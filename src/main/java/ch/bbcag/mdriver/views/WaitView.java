package ch.bbcag.mdriver.views;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.StateMessage;
import ch.bbcag.mdriver.connection.Connection;
import ch.bbcag.mdriver.scenes.ItemBuilder;
import ch.bbcag.mdriver.scenes.LibraryScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;
import static ch.bbcag.mdriver.common.StateCode.ABORT_RECORDING;

public class WaitView extends VBox {
    public WaitView(LibraryScene scene) {
        scene.getPopUpPane().setDisable(false);
        scene.getPopUpPane().setVisible(true);

        setBorder(ItemBuilder.buildBorder(Constants.COLOR_BLUE, Constants.ROUNDED_CORNER));
        setBackground(ItemBuilder.buildBackground(Color.WHITE, Constants.ROUNDED_CORNER));
        setOpacity(100);

        maxHeightProperty().bind(scene.heightProperty().multiply(0.8));
        maxWidthProperty().bind(scene.widthProperty().multiply(0.7));
        setAlignment(Pos.CENTER);

        Text message = ItemBuilder.buildText("Bitte warten\ndie Aufnahmen wird\nabgespielt\n", DEFAULT_FONT_SIZE * 2, Constants.COLOR_BLUE);
        message.setTextAlignment(TextAlignment.CENTER);

        Button abort = ItemBuilder.buildButton("Abbrechen", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_RED);
        abort.setOnAction(event -> {
            Connection.getInstance().send(new StateMessage(ABORT_RECORDING));
            scene.getPopUpPane().setDisable(true);
            scene.getPopUpPane().setVisible(false);
        });

        getChildren().addAll(message, abort);

    }
}
