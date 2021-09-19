package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.connection.Connection;
import ch.bbcag.mdriver.input.ControllerManager;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.Parameterizable;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.navigation.parameters.ConnectParams;
import ch.bbcag.mdriver.navigation.parameters.ControllerParams;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import net.java.games.input.Controller;

import java.util.List;

public class ConnectScene extends BaseScene implements Initializable, Parameterizable<ConnectParams> {

    private final Text message;
    private String host;

    public ConnectScene(Navigator nav) {
        super(nav);

        pane.setBackground(new Background(new BackgroundFill(Constants.COLOR_BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        VBox centerBox = new VBox();

        centerBox.setAlignment(Pos.CENTER);
        centerBox.spacingProperty().bind(pane.heightProperty().divide(10));

        Text title = ItemBuilder.buildText("Bitte warten", 100, Color.WHITE);

        message = ItemBuilder.buildText("Die Verbindung zum Roboter wird aufgebaut", 40, Color.WHITE);
        centerBox.getChildren().addAll(title, message);

        pane.setCenter(centerBox);
    }

    @Override
    public void initialize() {
        message.setText("Die Verbindung zum Roboter wird aufgebaut");
        new Thread(() -> {
            Connection.getInstance(host, 18999);
            if (Connection.isConnected()) {

                List<Controller> controllers = ControllerManager.getAllControllers();
                if (controllers.size() > 1) {
                    Platform.runLater(() -> navigator.navigateTo(SceneType.CONTROLLER_SELECT));
                } else if (controllers.size() == 1) {
                    Platform.runLater(() -> navigator.navigateToWithParams(SceneType.DRIVE, new ControllerParams(controllers.get(0))));
                } else {
                    Platform.runLater(() -> navigator.navigateTo(SceneType.DRIVE));
                }
            } else {
                Platform.runLater(() -> message.setText("Die Verbindung konnte nicht hergestellt werden"));
                new Thread(() -> {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> navigator.navigateTo(SceneType.START));
                }).start();
            }
        }).start();
    }

    @Override
    public void onNavigate(ConnectParams params) {
        this.host = params.getHost();
    }
}
