package ch.bbcag.mdriver.scenes;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.navigation.Initializable;
import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import ch.bbcag.mdriver.navigation.parameters.ConnectParams;
import ch.bbcag.mdriver.views.ButtonBoxView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ch.bbcag.mdriver.Constants.DEFAULT_FONT_SIZE;

public class StartScene extends BaseScene implements Initializable {

    private final TextField ipAddress;

    public StartScene(Navigator nav) {
        super(nav);

        Text title = ItemBuilder.buildText("Mindstorms Driver", 100, Constants.COLOR_BLUE);

        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.getChildren().add(title);
        textFlow.setPadding(new Insets(0, 0, 80, 0));

        ipAddress = ItemBuilder.buildTextField("IP-Adresse", Constants.COLOR_BLUE, DEFAULT_FONT_SIZE, 445);
        ipAddress.setOnAction(event -> {
            if (checkIP(ipAddress.getText())) {
                navigator.navigateToWithParams(SceneType.CONNECT, new ConnectParams(ipAddress.getText()));
            }
        });

        Button connect = ItemBuilder.buildButton("Verbinden", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        connect.setOnAction(event -> ipAddress.getOnAction());

        VBox inputBox = new VBox();
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(34);
        inputBox.getChildren().addAll(textFlow, ipAddress, connect);

        pane.setCenter(inputBox);

        Button recordings = ItemBuilder.buildButton("Bibliothek", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        recordings.setOnAction(event -> nav.navigateTo(SceneType.LIBRARY_START));

        Button help = ItemBuilder.buildButton("Hilfe", DEFAULT_FONT_SIZE, Color.WHITE, Constants.COLOR_BLUE);
        help.setOnAction(event -> nav.navigateTo(SceneType.HELP_START));

        ButtonBoxView buttonBox = new ButtonBoxView(pane);
        buttonBox.getChildren().addAll(recordings, help);
        buttonBox.spacingProperty().bind(buttonBox.prefWidthProperty().subtract(recordings.getWidth() + help.getWidth()).divide((long) buttonBox.getChildren().size() * 3));

        pane.setBottom(buttonBox);
    }

    private boolean checkIP(String ip) {
        if (!ip.equals("")) {
            Pattern pattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(ip);
            return matcher.find();
        } else {
            return false;
        }
    }

    @Override
    public void initialize() {
        ipAddress.setText("");
    }
}
