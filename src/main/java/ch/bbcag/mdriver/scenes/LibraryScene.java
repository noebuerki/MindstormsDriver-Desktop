package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.Record;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.record.RecordManager;
import ch.bbcag.mdriver.views.BubbleUpdater;
import ch.bbcag.mdriver.views.ButtonBoxView;
import ch.bbcag.mdriver.views.RecordView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;

public class LibraryScene extends BaseScene implements Initializable {

    private final BorderPane popUpPane;
    private final TilePane tilePane;
    private final Circle bubble;
    private final boolean playable;

    public LibraryScene(Navigator nav, SceneType backTarget, boolean playable) {
        super(nav);
        this.playable = playable;

        popUpPane = new BorderPane();
        popUpPane.setOpacity(Double.MAX_VALUE);
        popUpPane.minWidthProperty().bind(pane.widthProperty());
        popUpPane.minHeightProperty().bind(pane.heightProperty());
        popUpPane.setDisable(true);
        popUpPane.setVisible(false);
        root.getChildren().add(popUpPane);


        HBox topBar = ItemBuilder.buildTopBar("Bibliothek", pane);
        bubble = (Circle) topBar.getChildren().get(1);
        pane.setTop(topBar);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.maxHeightProperty().bind(pane.heightProperty().multiply(0.7));
        scrollPane.maxWidthProperty().bind(pane.widthProperty().multiply(0.9));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(ItemBuilder.buildBackground(Color.WHITE));
        scrollPane.setBorder(new Border(new BorderStroke(Constants.COLOR_BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        tilePane = new TilePane();
        tilePane.prefWidthProperty().bind(scrollPane.widthProperty());
        tilePane.minHeightProperty().bind(scrollPane.heightProperty().subtract(4));
        tilePane.setHgap(50);
        tilePane.setVgap(80);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(Constants.TILE_INSET);
        tilePane.setBackground(ItemBuilder.buildBackground(Color.WHITE));

        scrollPane.setContent(tilePane);

        pane.setCenter(scrollPane);
        Button back = ItemBuilder.buildButton(Constants.replaceSwissChars("Zurück"), DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);

        back.setOnAction(event -> nav.navigateTo(backTarget));


        ButtonBoxView buttonBox = new ButtonBoxView(pane);

        buttonBox.getChildren().add(back);

        pane.setBottom(buttonBox);
    }

    public BorderPane getPopUpPane() {
        return popUpPane;
    }

    @Override
    public void initialize() {
        BubbleUpdater.getInstance().setBubble(bubble);

        tilePane.getChildren().removeAll(tilePane.getChildren());

        RecordManager manager = new RecordManager();
        List<Record> recordings = manager.getRecords();

        if (recordings.isEmpty()) {
            StackPane emptyPane = new StackPane();
            emptyPane.setBackground(ItemBuilder.buildBackground(Constants.COLOR_BLUE, Constants.ROUNDED_CORNER));
            emptyPane.setPadding(Constants.TILE_INSET);
            emptyPane.getChildren().add(ItemBuilder.buildText("Es sind keine\nAufnahmen vorhanden.", DEFAULT_FONT_SIZE * 2, Color.WHITE));

            tilePane.getChildren().add(emptyPane);
        } else {
            recordings.forEach(record -> tilePane.getChildren().add(new RecordView(record, playable, this)));
        }
    }
}
