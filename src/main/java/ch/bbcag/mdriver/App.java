package ch.bbcag.mdriver;

import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.scenes.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(Constants.WINDOW_MIN_WIDTH);
        primaryStage.setMinHeight(Constants.WINDOW_MIN_HEIGHT);
        primaryStage.getIcons().add(new Image("/images/logo.png"));
        primaryStage.setTitle("Mindstorms Driver");

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        Navigator navigator = new Navigator(primaryStage);

        navigator.registerScene(SceneType.START, new StartScene(navigator));
        navigator.registerScene(SceneType.CONNECT, new ConnectScene(navigator));
        navigator.registerScene(SceneType.CONTROLLER_SELECT, new ControllerSelectScene(navigator));
        navigator.registerScene(SceneType.DRIVE, new DriveScene(navigator));
        navigator.registerScene(SceneType.LIBRARY_DRIVE, new LibraryScene(navigator, SceneType.DRIVE, true));
        navigator.registerScene(SceneType.LIBRARY_START, new LibraryScene(navigator, SceneType.START, false));
        navigator.registerScene(SceneType.HELP_DRIVE, new HelpScene(navigator, SceneType.DRIVE));
        navigator.registerScene(SceneType.HELP_START, new HelpScene(navigator, SceneType.START));

        navigator.navigateTo(SceneType.START);
    }
}
