package ch.bbcag.mdriver.common;

public abstract class Message {

    protected final MessageType type;

    protected Long time;

    protected Message(MessageType type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime() {
        time = System.currentTimeMillis();
    }

    public MessageType getType() {
        return type;
    }
}
