package ch.bbcag.mdriver.connection;

import ch.bbcag.mdriver.common.Message;

public interface ReceiveListener {
    void onReceive(Message message);
}
