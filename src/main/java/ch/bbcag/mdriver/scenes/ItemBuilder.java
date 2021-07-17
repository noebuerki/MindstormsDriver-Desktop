package ch.bbcag.mdriver.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

import static ch.bbcag.mdriver.Constants.*;

public class ItemBuilder {

    private static final String ROBOTO = Objects.requireNonNull(ItemBuilder.class.getResource("/font/Roboto-Regular.ttf")).toExternalForm();

    public static Text buildText(String promptText, int fontSize, Color fontColor) {
        Text text = buildText(promptText, fontSize);
        text.setFill(fontColor);
        return text;
    }

    public static Text buildText(String promptText, int fontSize) {
        Text text = new Text(promptText);

        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.loadFont(ROBOTO, fontSize));

        return text;
    }

    public static TextField buildTextField(String promptText, Color borderColor, int fontSize, int maxWidth) {
        TextField textField = buildTextField(promptText, borderColor, fontSize);
        textField.setMaxWidth(maxWidth);
        return textField;
    }

    public static TextField buildTextField(String promptText, Color borderColor, int fontSize) {
        TextField textField = new TextField();

        textField.setPromptText(promptText);

        textField.setFont(Font.loadFont(ROBOTO, fontSize));

        textField.setBorder(buildBorder(borderColor, ROUNDED_CORNER, BorderStroke.MEDIUM));
        textField.setStyle("-fx-text-box-border: transparent; -fx-faint-focus-color: transparent; -fx-focus-color: transparent;");

        return textField;
    }

    public static Background buildBackground(Color color) {
        return buildBackground(color, CornerRadii.EMPTY);
    }

    public static Background buildBackground(Color color, CornerRadii cornerRadii) {
        return new Background(new BackgroundFill(color, cornerRadii, Insets.EMPTY));
    }

    public static Button buildButton(String text, int fontSize, Color fontColor, Color background) {
        return buildButton(text, fontSize, fontColor, background, DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT);
    }

    public static Button buildButton(String text, int fontSize, Color fontColor, Color background, int width, int height) {
        Button button = new Button(text);

        button.setBackground(buildBackground(background, ROUNDED_CORNER));
        button.setFont(Font.loadFont(ROBOTO, fontSize));
        button.setTextFill(fontColor);
        button.setFocusTraversable(false);
        button.setAlignment(Pos.CENTER);

        button.setTextAlignment(TextAlignment.CENTER);

        button.setMinSize(width, height);
        button.setPrefSize(width, height);

        return button;
    }

    public static HBox buildTopBar(String title, Pane pane) {
        HBox bar = new HBox();
        bar.setBackground(ItemBuilder.buildBackground(COLOR_BLUE));
        bar.setPrefHeight(TOP_BAR_HEIGHT);
        bar.prefWidthProperty().bind(pane.widthProperty());
        bar.setPadding(new Insets(0, HORIZONTAL_INSET_SIZE, 0, HORIZONTAL_INSET_SIZE));
        bar.setAlignment(Pos.CENTER);

        Text text = buildText(title, DEFAULT_FONT_SIZE, Color.WHITE);

        Circle status = new Circle(TOP_BAR_HEIGHT / 4d, COLOR_GREEN);

        bar.spacingProperty().bind(pane.widthProperty().subtract(status.getRadius() * 2 + text.getLayoutBounds().getWidth() + bar.getInsets().getRight() + bar.getInsets().getLeft()));

        bar.getChildren().addAll(text, status);
        return bar;
    }

    public static Border buildBorder(Color color, CornerRadii radii, BorderWidths widths) {
        return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, radii, widths));
    }

    public static Border buildBorder(Color color, CornerRadii radii) {
        return buildBorder(color, radii, BorderWidths.DEFAULT);
    }

    public static Border buildBorder(Color color) {
        return buildBorder(color, CornerRadii.EMPTY, BorderWidths.DEFAULT);
    }
}
