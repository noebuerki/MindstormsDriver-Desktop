package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.input.ControllerManager;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.navigation.parameters.ControllerParams;
import ch.bbcag.mdriver.views.BubbleUpdater;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.java.games.input.Controller;

import java.util.List;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;

public class ControllerSelectScene extends BaseScene implements Initializable {
    private final VBox controllerBox;

    private final Circle bubble;

    public ControllerSelectScene(Navigator nav) {
        super(nav);

        HBox topBar = ItemBuilder.buildTopBar(Constants.replaceSwissChars("Controller wählen"), pane);
        bubble = (Circle) topBar.getChildren().get(1);
        pane.setTop(topBar);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.maxWidthProperty().bind(pane.widthProperty().multiply(0.9));
        scrollPane.maxHeightProperty().bind(pane.heightProperty().multiply(0.8));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(ItemBuilder.buildBackground(Color.WHITE));
        scrollPane.setBorder(ItemBuilder.buildBorder(Constants.COLOR_BLUE));

        controllerBox = new VBox();
        controllerBox.minWidthProperty().bind(scrollPane.widthProperty());
        controllerBox.maxWidthProperty().bind(scrollPane.widthProperty());
        controllerBox.minHeightProperty().bind(scrollPane.heightProperty().subtract(4));
        controllerBox.spacingProperty().bind(controllerBox.heightProperty().divide(20));
        controllerBox.setAlignment(Pos.BASELINE_CENTER);
        controllerBox.setPadding(Constants.TILE_INSET);
        controllerBox.setBackground(ItemBuilder.buildBackground(Color.WHITE));

        scrollPane.setContent(controllerBox);

        pane.setCenter(scrollPane);
    }

    @Override
    public void initialize() {

        BubbleUpdater.getInstance().setBubble(bubble);

        List<Controller> controllers = ControllerManager.getAllControllers();

        controllerBox.getChildren().removeAll(controllerBox.getChildren());

        for (Controller controller : controllers) {
            Button button = ItemBuilder.buildButton(controller.getName(), DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);

            button.prefWidthProperty().bind(controllerBox.widthProperty().subtract(2));
            button.prefWidthProperty().bind(controllerBox.widthProperty().multiply(0.9));

            button.setOnAction(event -> navigator.navigateToWithParams(SceneType.DRIVE, new ControllerParams(controller)));

            controllerBox.getChildren().add(button);
        }
    }
}
