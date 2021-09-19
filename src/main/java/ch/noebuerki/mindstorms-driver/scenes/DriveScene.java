package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.DriveMessage;
import ch.bbcag.mdriver.common.Record;
import ch.bbcag.mdriver.connection.Connection;
import ch.bbcag.mdriver.input.ControllerManager;
import ch.bbcag.mdriver.input.InputHandler;
import ch.bbcag.mdriver.map.MapDrawer;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.Parameterizable;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.navigation.parameters.ControllerParams;
import ch.bbcag.mdriver.record.RecordManager;
import ch.bbcag.mdriver.views.BubbleUpdater;
import ch.bbcag.mdriver.views.ButtonBoxView;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;

public class DriveScene extends BaseScene implements Initializable, Parameterizable<ControllerParams> {

    private final Circle bubble;
    private final ImageView mapView;
    private final RecordManager recordManager = new RecordManager();
    private final ControllerManager controllerManager;
    private final AnimationTimer mapTimer;
    private Thread controllerManagerThread;

    public DriveScene(Navigator nav) {
        super(nav);

        controllerManager = new ControllerManager(nav);
        controllerManagerThread = new Thread(controllerManager);

        HBox topBar = ItemBuilder.buildTopBar("Fahransicht", pane);
        bubble = (Circle) topBar.getChildren().get(1);
        pane.setTop(topBar);


        final long[] lastUpdate = {0};
        MapDrawer mapDrawer = new MapDrawer();

        mapTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (recordManager.isRecording()) {
                    if (lastUpdate[0] == 0 || now - lastUpdate[0] >= Constants.MAP_REFRESH_TIME) {
                        Record record = recordManager.getRecord();
                        record.setTime();
                        mapView.setImage(mapDrawer.draw(record));
                        lastUpdate[0] = now;
                    }
                }
            }
        };

        VBox mapBox = new VBox();
        mapBox.maxHeightProperty().bind(pane.heightProperty().multiply(0.7));
        mapBox.maxWidthProperty().bind(pane.widthProperty().multiply(0.9));
        mapBox.setBorder(new Border(new BorderStroke(Constants.COLOR_BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        mapBox.setAlignment(Pos.CENTER);

        mapView = new ImageView();
        mapView.setPreserveRatio(true);
        mapView.fitHeightProperty().bind(mapBox.maxHeightProperty());


        mapBox.getChildren().add(mapView);
        pane.setCenter(mapBox);
        Button start = ItemBuilder.buildButton("Aufnehmen", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        start.setOnAction(event -> handleRecording(start));


        Button recordings = ItemBuilder.buildButton("Bibliothek", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        recordings.setOnAction(event -> {
            if (recordManager.isRecording()) {
                handleRecording(start);
            }
            Connection.getInstance().send(new DriveMessage(0, 0));
            controllerManager.shouldStop(true);
            nav.navigateTo(SceneType.LIBRARY_DRIVE);
        });


        Button help = ItemBuilder.buildButton("Hilfe", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        help.setOnAction(event -> {
            if (recordManager.isRecording()) {
                handleRecording(start);
            }
            Connection.getInstance().send(new DriveMessage(0, 0));
            controllerManager.shouldStop(true);
            nav.navigateTo(SceneType.HELP_DRIVE);
        });

        ButtonBoxView buttonBox = new ButtonBoxView(pane);
        buttonBox.prefWidthProperty().bind(pane.widthProperty());
        buttonBox.getChildren().addAll(recordings, start, help);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.spacingProperty().bind((buttonBox.prefWidthProperty().subtract(recordings.getWidth() + start.getWidth() + help.getWidth())).divide(buttonBox.getChildren().size() * 3));

        pane.setBottom(buttonBox);
    }

    private void handleRecording(Button start) {
        if (recordManager.isRecording()) {
            if (!recordManager.getRecord().getMessages().isEmpty()) {
                recordManager.saveRecording(Constants.GSON);
            } else {
                recordManager.endRecording();
            }
            mapTimer.stop();
            mapView.setImage(null);
            start.setBackground(ItemBuilder.buildBackground(Constants.COLOR_BLUE, new CornerRadii(8)));
            start.setText("Aufnehmen");
        } else {
            recordManager.beginRecording();
            mapTimer.start();
            start.setText("Beenden");
            start.setBackground(ItemBuilder.buildBackground(Constants.COLOR_RED, new CornerRadii(8)));
        }
    }

    @Override
    public void initialize() {
        InputHandler inputHandler = new InputHandler(controllerManager);

        BubbleUpdater.getInstance().setBubble(bubble);
        controllerManager.setControllerListener(inputHandler);

        this.setOnKeyPressed(inputHandler);
        this.setOnKeyReleased(inputHandler);
        Connection.getInstance().addOnSendListener(recordManager);

        if (!controllerManager.isRunning()) {
            controllerManager.isRunning(true);
        }

        if (!controllerManagerThread.isAlive()) {
            controllerManager.shouldStop(false);
            controllerManagerThread = new Thread(controllerManager);
            controllerManagerThread.start();
        }


    }

    @Override
    public void onNavigate(ControllerParams params) {
        controllerManager.setController(params.getController());
    }
}
