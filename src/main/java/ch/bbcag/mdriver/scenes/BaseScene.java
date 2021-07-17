package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.navigation.Navigator;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public abstract class BaseScene extends Scene {

    protected final Group root;
    protected Navigator navigator;
    protected BorderPane pane;

    private BaseScene(Group group) {
        super(group);
        root = group;
    }

    public BaseScene(Navigator navigator) {
        this(new Group());

        this.navigator = navigator;
        pane = new BorderPane();

        root.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/style.css")).toExternalForm());

        root.getChildren().add(pane);

        pane.minWidthProperty().bind(widthProperty());
        pane.prefWidthProperty().bind(widthProperty());
        pane.maxWidthProperty().bind(widthProperty());

        pane.minHeightProperty().bind(heightProperty());
        pane.prefHeightProperty().bind(heightProperty());
        pane.maxHeightProperty().bind(heightProperty());
    }
}
