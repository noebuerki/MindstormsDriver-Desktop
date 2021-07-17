package ch.bbcag.mdriver.views;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.connection.Connection;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Circle;

public class BubbleUpdater extends AnimationTimer {

    private static BubbleUpdater updater;
    private Circle bubble;

    private BubbleUpdater() {
    }

    public static BubbleUpdater getInstance() {
        if (updater == null) {
            updater = new BubbleUpdater();
            updater.start();
        }
        return updater;
    }

    @Override
    public void handle(long now) {
        if (bubble != null) {
            if (Connection.isConnected()) {
                bubble.setFill(Constants.COLOR_GREEN);
            } else {
                bubble.setFill(Constants.COLOR_RED);
            }
        }
    }

    public void setBubble(Circle bubble) {
        this.bubble = bubble;
    }
}
