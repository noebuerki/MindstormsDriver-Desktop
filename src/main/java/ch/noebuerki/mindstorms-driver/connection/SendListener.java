package ch.bbcag.mdriver.connection;

import ch.bbcag.mdriver.common.Message;

public interface SendListener {
    void onSend(Message message);
}
