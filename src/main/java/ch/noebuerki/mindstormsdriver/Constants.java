package ch.bbcag.mdriver;

import ch.bbcag.mdriver.common.GsonCreator;
import com.google.gson.Gson;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


public class Constants {
    public static final int WINDOW_MIN_WIDTH = 900;
    public static final int WINDOW_MIN_HEIGHT = 650;

    public static final Color COLOR_BLUE = Color.valueOf("#4B72A6");
    public static final Color COLOR_GREEN = Color.valueOf("#77E1B3");
    public static final Color COLOR_RED = Color.valueOf("#DB5C7A");

    public static final Gson GSON = GsonCreator.create();

    public static final CornerRadii ROUNDED_CORNER = new CornerRadii(8);

    public static final int DEFAULT_BUTTON_WIDTH = 200;
    public static final int DEFAULT_BUTTON_HEIGHT = 50;

    public static final int HORIZONTAL_INSET_SIZE = 80;
    public static final int VERTICAL_INSET_SIZE = 80;

    public static final int TOP_BAR_HEIGHT = 80;
    public static final int DEFAULT_FONT_SIZE = 28;
    public static final int RECORD_ACTION_BUTTON_SIZE = 57;

    public static final Insets BOTTOM_BOX_INSET = new Insets(20, 0, 20, 0);
    public static final Insets TILE_INSET = new Insets(40);
    public static final long MAP_REFRESH_TIME = 500000000L;

    public static String replaceSwissChars(String input) {
        return input.replaceAll("ü", "\u00FC")
                .replaceAll("ä", "\u00E4");
    }
}
