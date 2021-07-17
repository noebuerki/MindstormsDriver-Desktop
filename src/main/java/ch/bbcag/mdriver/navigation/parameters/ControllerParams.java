package ch.bbcag.mdriver.navigation.parameters;

import net.java.games.input.Controller;

public class ControllerParams extends BaseParams {
    private final Controller controller;

    public ControllerParams(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }
}
