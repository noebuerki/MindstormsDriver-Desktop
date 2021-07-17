package ch.bbcag.mdriver.map;


import ch.bbcag.mdriver.common.DriveMessage;
import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.Record;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MapDrawer {

    public final static float FACTOR = 1.5f;
    public final static float WHEEL_DIAMETER = 110 * FACTOR;
    public final static float WIDTH = 44 * FACTOR;

    public final static int START_POINT_OFFSET = 2;
    public final static int IMAGE_MARGIN = (int) (WHEEL_DIAMETER * 2);

    public final static int FULL_CIRCLE = 360;

    private int x;
    private int y;
    private int angle;
    private int newX;
    private int newY;
    private int newAngle;
    private Graphics2D g;

    public Image draw(Record record) {
        List<Message> records = record.getMessages();

        if (records.size() < 1) {
            return null;
        }

        x = 0;
        y = 0;
        reset();

        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;

        for (int i = 0; i < records.size(); i++) {
            executeMessageWhenAvailable(record, records, i, false);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return drawImage(record, minX, minY, maxX, maxY);
    }

    private double getDeltaTime(Record record, int i) {
        DriveMessage driveMessage = (DriveMessage) record.getMessages().get(i);
        if (i >= record.getMessages().size() - 1) {
            return (record.getTime() - driveMessage.getTime()) * 0.001;
        }
        return (record.getMessages().get(i + 1).getTime() - driveMessage.getTime()) * 0.001;
    }

    private void interpretMessage(DriveMessage driveMessage, double deltaTime, boolean shouldDraw) {
        if (driveMessage.getLeft() == driveMessage.getRight()) {
            if (driveMessage.getLeft() == 0) {
                return;
            }
            drawLine(driveMessage.getLeft(), deltaTime, shouldDraw);
        } else {
            drawArc(driveMessage.getLeft(), driveMessage.getRight(), deltaTime, shouldDraw);
        }
    }

    private Image drawImage(Record record, int minX, int minY, int maxX, int maxY) {
        int width = maxX - minX + IMAGE_MARGIN;
        int height = maxY - minY + IMAGE_MARGIN;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        int startX = Math.abs(minX) + IMAGE_MARGIN / 2;
        int startY = Math.abs(minY) + IMAGE_MARGIN / 2;

        x = startX;
        y = startY;
        reset();

        loadGraphics(image);

        List<Message> records = record.getMessages();

        for (int i = 0; i < records.size(); i++) {
            executeMessageWhenAvailable(record, records, i, true);
        }

        g.setPaint(Color.RED);
        g.fillArc(startX - START_POINT_OFFSET, startY - START_POINT_OFFSET, START_POINT_OFFSET * 2, START_POINT_OFFSET * 2, 0, FULL_CIRCLE);

        return SwingFXUtils.toFXImage(image, null);
    }

    private void executeMessageWhenAvailable(Record record, List<Message> records, int i, boolean shouldDraw) {
        Message message = records.get(i);
        if (!(message instanceof DriveMessage)) return;
        DriveMessage driveMessage = (DriveMessage) message;

        double deltaTime = getDeltaTime(record, i);

        interpretMessage(driveMessage, deltaTime, shouldDraw);

        x = newX;
        y = newY;
        angle = newAngle;
    }

    private void reset() {
        g = null;
        newX = x;
        newY = y;
        angle = FULL_CIRCLE / 2;
        newAngle = angle;
    }

    private void loadGraphics(BufferedImage image) {
        g = image.createGraphics();
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        g.setPaint(Color.BLACK);
    }

    private void drawLine(float speed, double deltaTime, boolean shouldDraw) { // draw a straight line

        float way = (float) (deltaTime * getMmPerSec(speed));

        newX = x + (int) Math.round(Math.sin(Math.toRadians(angle)) * way);
        newY = y + (int) Math.round(Math.cos(Math.toRadians(angle)) * way);

        if (shouldDraw) g.drawLine(x, y, newX, newY);
    }

    /**
     * @param deltaTime  is how long it was driven at the speed left and right
     * @param shouldDraw defines if a arc should be drawn
     */
    private void drawArc(float left, float right, double deltaTime, boolean shouldDraw) { // draw a arc
        float radius = getInnerRadius(left, right) + WIDTH / 2;
        float midSpeed = getMidSpeed(left, right);

        int startAngle = angle;
        int sign = 1; // says whether the circle should be drawn left or right from the start
        int angleSign = -1; // tells in which direction the circle should be drawn

        if (left < right) {
            sign = -sign;
            startAngle -= FULL_CIRCLE / 2;
            angleSign = -angleSign;
        }

        if (left < 0) {
            startAngle -= FULL_CIRCLE / 2;
            sign = -sign;
        }

        if (left * right < 0) {
            angleSign -= angleSign;
        }

        float circleMidX = (float) (x - Math.cos(Math.toRadians(angle)) * radius * sign);
        float circleMidY = (float) (y + Math.sin(Math.toRadians(angle)) * radius * sign);

        int circleX = Math.round(circleMidX - radius);
        int circleY = Math.round(circleMidY - radius);

        // calculates how many degrees the robot has rotated
        double circleCircumference = radius * 2 * Math.PI;
        double way = midSpeed * deltaTime;
        double angleRotation = way / circleCircumference * 360;

        int size = Math.round(radius * 2);

        if (shouldDraw)
            g.drawArc(circleX, circleY, size, size, startAngle, (int) Math.round(Math.min(FULL_CIRCLE, angleRotation) * angleSign));

        newAngle = (int) Math.round((angle + angleRotation * angleSign) % FULL_CIRCLE); // the new angle of the roboter


        /*
        help solution on [this](docs/assets/ArcAid.svg) image
         */
        int deltaAngle = newAngle - angle;

        double hypotenuse = 2 * radius * Math.sin(Math.toRadians(deltaAngle / 2d));

        int a = (FULL_CIRCLE / 4 - Math.abs((angle + FULL_CIRCLE / 4) % FULL_CIRCLE));
        int b = FULL_CIRCLE / 4 - deltaAngle / 2;
        int tendonAngle = a + b;

        int dX = (int) Math.round(Math.cos(Math.toRadians(tendonAngle)) * hypotenuse);
        int dY = (int) Math.round(Math.sin(Math.toRadians(tendonAngle)) * hypotenuse);

        dX *= angleSign;
        dY *= angleSign;

        newX += dX;
        newY += dY;
    }


    private double getMmPerSec(float speed) {
        return WHEEL_DIAMETER * Math.PI * (speed / FULL_CIRCLE);
    }


    private float getMidSpeed(float leftSpeed, float rightSpeed) {
        return (float) ((getMmPerSec(leftSpeed) + getMmPerSec(rightSpeed)) / 2);
    }

    /**
     * @return the inner radius of the Mindstorms when he has a left speed of leftSpeed and a right speed of right speed
     */
    private float getInnerRadius(float leftSpeed, float rightSpeed) {
        double left = getMmPerSec(leftSpeed);
        double right = getMmPerSec(rightSpeed);

        double min = Math.min(left, right);
        double max = Math.max(left, right);

        return (float) ((WIDTH * min) / (max - min));
    }

}
