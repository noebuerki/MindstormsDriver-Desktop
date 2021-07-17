import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.DriveMessage;
import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.StateCode;
import ch.bbcag.mdriver.common.StateMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GsonParserTest {
    @Test
    public void toJsonTest() {
        Message message = new DriveMessage(100, 200);

        String json = Constants.GSON.toJson(message);

        String expected = "{\"l\":100.0,\"r\":200.0,\"type\":\"drive\"}";
        assertEquals(expected, json);
    }

    @Test
    public void fromJsonTest() {
        String input = "{\"l\":200.0,\"r\":-200.0,\"type\":\"drive\"}";

        DriveMessage message = (DriveMessage) Constants.GSON.fromJson(input, Message.class);

        assertEquals(message.getLeft(), 200);
        assertEquals(message.getRight(), -200);
        assertNull(message.getTime());
    }

    @ParameterizedTest
    @CsvSource(value = {"{'type':'state'};STATE", "{'type':'drive'};DRIVE", "{'type':'shoot'};SHOOT", "{'type':'record'};RECORD"}, delimiter = ';')
    public void typeTest(String input, String expected) {
        Message message = Constants.GSON.fromJson(input, Message.class);
        assertEquals(expected, message.getType().toString());
    }

    @Test
    public void driveSpeedTest() {
        String input = "{'type':'drive', 'l':-100, 'r':100}";

        Message message = Constants.GSON.fromJson(input, Message.class);

        assertEquals(DriveMessage.class, message.getClass());
        assertEquals(-100, ((DriveMessage) message).getLeft());
        assertEquals(100, ((DriveMessage) message).getRight());
    }

    @ParameterizedTest
    @CsvSource(value = {"{'type':'state', 'code':13};SERVER_CLOSING", "{'type':'state', 'code':200};CONNECTION_OK", "{'type':'state', 'code':201};RECORDING_ENDED"}, delimiter = ';')
    public void readStateCode(String input, String expected) {
        StateMessage message = (StateMessage) Constants.GSON.fromJson(input, Message.class);
        assertEquals(expected, message.getStateCode().toString());
    }

    @Test
    public void parseStateCode() {
        String message = Constants.GSON.toJson(StateCode.CONNECTION_OK);
        assertEquals("200", message);
    }
}
