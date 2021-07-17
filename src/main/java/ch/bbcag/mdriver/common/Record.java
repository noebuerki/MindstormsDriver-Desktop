package ch.bbcag.mdriver.common;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Record extends Message {

    private final List<Message> messages = new ArrayList<>();

    @SerializedName(value = "date")
    private Date saveDate;

    public Record() {
        super(MessageType.RECORD);
    }

    public void add(Message e) {
        e.setTime();
        messages.add(e);
    }

    public void endRecord() {

        saveDate = new Date();
        setTime();
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

}
