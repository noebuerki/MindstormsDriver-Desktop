package ch.bbcag.mdriver.views;

import ch.bbcag.mdriver.common.Record;
import ch.bbcag.mdriver.map.MapDrawer;
import ch.bbcag.mdriver.scenes.ItemBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static ch.bbcag.mdriver.Constants.*;

public class MapView extends StackPane {

    public MapView(Record record) {

        setMaxSize(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT);
        setMinSize(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT);

        StackPane rootNode = new StackPane();
        rootNode.setBorder(ItemBuilder.buildBorder(COLOR_BLUE));
        rootNode.setBackground(ItemBuilder.buildBackground(Color.WHITE));

        setAlignment(Pos.CENTER);
        setBackground(ItemBuilder.buildBackground(Color.WHITE));

        MapDrawer drawer = new MapDrawer();
        ImageView path = new ImageView(drawer.draw(record));
        path.setPreserveRatio(true);

        rootNode.maxWidthProperty().bind(maxWidthProperty().subtract(HORIZONTAL_INSET_SIZE * 2));
        rootNode.maxHeightProperty().bind(maxHeightProperty().subtract(VERTICAL_INSET_SIZE * 2));

        path.fitHeightProperty().bind(rootNode.maxHeightProperty());
        path.fitWidthProperty().bind(rootNode.maxWidthProperty());

        StackPane.setMargin(rootNode, new Insets(VERTICAL_INSET_SIZE, HORIZONTAL_INSET_SIZE, VERTICAL_INSET_SIZE, HORIZONTAL_INSET_SIZE));

        getChildren().add(rootNode);
        rootNode.getChildren().add(path);
    }
}
