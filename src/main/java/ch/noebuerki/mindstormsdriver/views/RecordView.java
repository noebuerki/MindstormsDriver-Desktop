package ch.bbcag.mdriver.views;

import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.Record;
import ch.bbcag.mdriver.common.StateCode;
import ch.bbcag.mdriver.common.StateMessage;
import ch.bbcag.mdriver.connection.Connection;
import ch.bbcag.mdriver.connection.ReceiveListener;
import ch.bbcag.mdriver.record.RecordManager;
import ch.bbcag.mdriver.scenes.ItemBuilder;
import ch.bbcag.mdriver.scenes.LibraryScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;

import static ch.bbcag.mdriver.Constants.*;

public class RecordView extends VBox {

    public static final int WIDTH = 252;
    public static final int HEIGHT = 224;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final Record record;
    private Stage dialog;

    public RecordView(Record record, boolean playable, LibraryScene scene) {
        this.record = record;
        ImageView imageView = new ImageView();

        Text textDate = ItemBuilder.buildText(dateFormat.format(record.getSaveDate()), DEFAULT_FONT_SIZE, COLOR_BLUE);

        Text textTime = ItemBuilder.buildText(timeFormat.format(record.getSaveDate()), DEFAULT_FONT_SIZE, COLOR_BLUE);

        Button play = ItemBuilder.buildButton("", DEFAULT_FONT_SIZE, Color.WHITE, COLOR_BLUE, RECORD_ACTION_BUTTON_SIZE, RECORD_ACTION_BUTTON_SIZE);
        play.setGraphic(new ImageView(new Image("images/play.png")));
        play.setOnAction(event -> {
            scene.getPopUpPane().getChildren().removeAll(scene.getPopUpPane().getChildren());
            scene.getPopUpPane().setCenter(new WaitView(scene));
            Connection.getInstance().send(record);
            Connection.getInstance().addOnReceiveListener(new ReceiveListener() {
                @Override
                public void onReceive(Message message) {
                    if (message instanceof StateMessage) {
                        if (((StateMessage) message).getStateCode() == StateCode.RECORDING_ENDED) {
                            Connection.getInstance().removeOnReceiveListener(this);
                            scene.getPopUpPane().setDisable(true);
                            scene.getPopUpPane().setVisible(false);
                            Connection.getInstance().removeOnReceiveListener(this);
                        }
                    }
                }
            });
        });
        Button map = ItemBuilder.buildButton("", DEFAULT_FONT_SIZE, Color.WHITE, COLOR_BLUE, RECORD_ACTION_BUTTON_SIZE, RECORD_ACTION_BUTTON_SIZE);
        map.setGraphic(new ImageView(new Image("images/map.png")));
        map.setOnAction(event -> showMap());
        Button delete = ItemBuilder.buildButton("", DEFAULT_FONT_SIZE, Color.WHITE, COLOR_RED, RECORD_ACTION_BUTTON_SIZE, RECORD_ACTION_BUTTON_SIZE);
        delete.setGraphic(new ImageView(new Image("images/delete.png")));
        delete.setOnAction(event -> {
            RecordManager.deleteRecord(record);
            scene.initialize();
        });

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(16);
        buttonBox.setPadding(new Insets(RECORD_ACTION_BUTTON_SIZE / 2, 0, RECORD_ACTION_BUTTON_SIZE / 2, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.maxWidthProperty().bind(widthProperty());
        if (playable) {
            buttonBox.getChildren().addAll(play, map, delete);
        } else {
            buttonBox.getChildren().addAll(map, delete);
        }

        setBorder(ItemBuilder.buildBorder(COLOR_BLUE));

        setPrefSize(WIDTH, HEIGHT);
        setAlignment(Pos.CENTER);
        setSpacing(10);

        getChildren().addAll(textDate, textTime, imageView, buttonBox);
    }

    private void showMap() {
        if (dialog == null) {
            dialog = new Stage();
            dialog.setTitle(String.format("Aufnahme von %s %s", dateFormat.format(record.getSaveDate()), timeFormat.format(record.getSaveDate())));


            Scene scene = new Scene(new MapView(record));
            dialog.setScene(scene);
        } else {
            dialog.requestFocus();
        }
        dialog.show();
    }
}
