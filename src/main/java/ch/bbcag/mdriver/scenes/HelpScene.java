package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.views.BubbleUpdater;
import ch.bbcag.mdriver.views.ButtonBoxView;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;

import java.util.Objects;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;

public class HelpScene extends BaseScene implements Initializable {

    private final Circle bubble;

    public HelpScene(Navigator nav, SceneType backTarget) {
        super(nav);

        HBox topBar = ItemBuilder.buildTopBar("Hilfe", pane);

        bubble = (Circle) topBar.getChildren().get(1);
        pane.setTop(topBar);

        HBox webBox = new HBox();
        webBox.maxHeightProperty().bind(pane.heightProperty().multiply(0.7));
        webBox.maxWidthProperty().bind(pane.widthProperty().multiply(0.9));
        webBox.setBorder(new Border(new BorderStroke(Constants.COLOR_BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        WebView webView = new WebView();
        webView.prefWidthProperty().bind(webBox.maxWidthProperty());
        webView.prefHeightProperty().bind(webBox.maxHeightProperty());

        webBox.getChildren().add(webView);

        pane.setCenter(webBox);

        webView.getEngine().load(Objects.requireNonNull(this.getClass().getResource("/markdown/markdownpage.html")).toString());

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                webView.getEngine().executeScript(String.format("load(%s)", "\"usermanual.md\""));
            }
        });

        Button back = ItemBuilder.buildButton(Constants.replaceSwissChars("Zurück"), DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        back.setOnAction(event -> nav.navigateTo(backTarget));

        ButtonBoxView buttonBox = new ButtonBoxView(pane);

        buttonBox.getChildren().add(back);

        pane.setBottom(buttonBox);
    }

    @Override
    public void initialize() {
        BubbleUpdater.getInstance().setBubble(bubble);
    }
}
