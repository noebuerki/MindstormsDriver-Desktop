package ch.bbcag.mdriver.connection;

import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.StateCode;
import ch.bbcag.mdriver.common.StateMessage;

public class ClosingServerManager implements ReceiveListener {

    @Override
    public void onReceive(Message message) {
        if (message instanceof StateMessage) {
            if (((StateMessage) message).getStateCode() == StateCode.SERVER_CLOSING) {
                Connection.getInstance().shouldReconnect(false);
                Connection.isConnected(false);
            }
        }
    }
}
