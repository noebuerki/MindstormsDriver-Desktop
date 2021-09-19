package ch.bbcag.mdriver.navigation;

import ch.bbcag.mdriver.navigation.parameters.BaseParams;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Navigator {

    private final Stage stage;
    private final Map<SceneType, Scene> sceneMap;
    private Scene activeScene;

    public Navigator(Stage stage) {

        this.stage = stage;
        sceneMap = new HashMap<>();
    }

    public void registerScene(SceneType type, Scene scene) {

        sceneMap.put(type, scene);
    }

    public <T extends BaseParams> void navigateToWithParams(SceneType type, T params) {
        activeScene = sceneMap.get(type);

        if (activeScene instanceof Parameterizable) {
            //noinspection unchecked
            ((Parameterizable<T>) activeScene).onNavigate(params);
        }
        navigateTo(type);
    }

    public void navigateTo(SceneType type) {

        activeScene = sceneMap.get(type);

        if (activeScene instanceof Initializable) {
            ((Initializable) activeScene).initialize();
        }

        stage.setScene(activeScene);
        stage.show();
    }
}
