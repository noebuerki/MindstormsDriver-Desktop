package ch.bbcag.mdriver.common;

public enum MessageType {
    @MessageClass(type = DriveMessage.class)
    DRIVE,
    @MessageClass(type = StateMessage.class)
    STATE,
    @MessageClass(type = ShootMessage.class)
    SHOOT,
    @MessageClass(type = Record.class)
    RECORD
}
