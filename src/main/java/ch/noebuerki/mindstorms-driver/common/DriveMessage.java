package ch.bbcag.mdriver.common;

import com.google.gson.annotations.SerializedName;

public class DriveMessage extends Message {

    @SerializedName(value = "r")
    private float rightSpeed;

    @SerializedName(value = "l")
    private float leftSpeed;

    public DriveMessage(float leftSpeed, float rightSpeed) {
        super(MessageType.DRIVE);
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    public void increaseRightSpeed(float speed) {
        rightSpeed += speed;
    }

    public void increaseLeftSpeed(float speed) {
        leftSpeed += speed;
    }

    public float getRight() {
        return rightSpeed;
    }

    public float getLeft() {
        return leftSpeed;
    }
}
