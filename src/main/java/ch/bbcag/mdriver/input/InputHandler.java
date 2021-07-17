package ch.bbcag.mdriver.input;

import ch.bbcag.mdriver.common.DriveMessage;
import ch.bbcag.mdriver.common.ShootMessage;
import ch.bbcag.mdriver.connection.Connection;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static net.java.games.input.Component.Identifier.Axis;
import static net.java.games.input.Component.Identifier.Button;

public class InputHandler implements ControllerListener, EventHandler<KeyEvent> {

    private final ControllerManager manager;

    private boolean pressedW;
    private boolean pressedS;
    private boolean pressedA;
    private boolean pressedD;
    private boolean pressedSpace;

    public InputHandler(ControllerManager manager) {
        this.manager = manager;
    }

    // General JInput Event Handler
    @Override
    public void onInput(Event event) {
        if (manager.getController().getType().equals(Controller.Type.GAMEPAD) ||
                manager.getController().getName().toLowerCase(Locale.ROOT).contains("controller") ||
                manager.getController().getName().toLowerCase(Locale.ROOT).contains("gamepad")) {
            withGamepad(event);
        } else if (manager.getController().getType().equals(Controller.Type.STICK)) {
            withStick(event);
        }
    }

    // JInput Stick Event Handler
    private void withStick(Event event) {
        if (event.getComponent().getIdentifier() instanceof Axis) {
            float data = event.getComponent().getPollData();

            if (data < 0.08 && data > -0.08) {
                data = 0;
            }

            manager.getComponentValues().replace(event.getComponent().getIdentifier(), data);
            Map<Component.Identifier, Float> componentValues = manager.getComponentValues();
            DriveMessage message = calculateMovementForStick(componentValues.get(Axis.X), componentValues.get(Axis.Y), componentValues.get(Axis.RZ));
            Connection connection = Connection.getInstance();
            connection.send(message);
        } else if (event.getComponent().getIdentifier() instanceof Button) {
            if (event.getComponent().getIdentifier().equals(Button._1) && event.getComponent().getPollData() == 1f) {

                Connection.getInstance().send(new ShootMessage());
            }
        }
    }

    public DriveMessage calculateMovementForStick(float x, float y, float z) {//https://www.impulseadventure.com/elec/robot-differential-steering.html
        x *= -80;
        y *= -80;

        Map<String, Double> rawValues = generalStickCalculation(x, y);

        double left = rawValues.get("left");
        double right = rawValues.get("right");

        if (left < 5 && left > -5) {
            left = 0;
        }
        if (right < 5 && right > -5) {
            right = 0;
        }

        if (right > 0 && left > 0) {
            double temp = right;
            right = left;
            left = temp;
        }

        if (z > 0.1f) {
            left += z * 20;
        } else if (z < -0.1f) {
            right += z * -20;
        }

        return new DriveMessage((float) left, (float) right);
    }

    // JInput Stick Event Handler
    private void withGamepad(Event event) {
        if (event.getComponent().getIdentifier() instanceof Axis) {
            float data = event.getComponent().getPollData();

            if (event.getComponent().getIdentifier().equals(Axis.RY)) {
                data += 1;
            } else if (event.getComponent().getIdentifier().equals(Axis.RX)) {
                data += 1;
            }

            if (data < 0.1 && data > -0.1) {
                data = 0;
            }

            manager.getComponentValues().replace(event.getComponent().getIdentifier(), data);
            Map<Component.Identifier, Float> componentValues = manager.getComponentValues();
            float y = componentValues.get(Axis.RY) - componentValues.get(Axis.RX);

            DriveMessage message = calculateMovementForGamepad(componentValues.get(Axis.X), y);

            Connection.getInstance().send(message);

        } else if (event.getComponent().getIdentifier() instanceof Button) {
            if (event.getComponent().getIdentifier().equals(Button._1) && event.getComponent().getPollData() == 1f) {

                Connection.getInstance().send(new ShootMessage());
            }
        }
    }

    public DriveMessage calculateMovementForGamepad(float x, float y) {
        x *= -40;
        y *= 40;

        Map<String, Double> rawValues = generalStickCalculation(x, y);

        double left = rawValues.get("left");
        double right = rawValues.get("right");

        if (left < 8 && left > -8) {
            left = 0;
        }
        if (right < 8 && right > -8) {
            right = 0;
        }
        if (y > 0) {
            double temp = left;
            left = right;
            right = temp;
        } else if (y == 0) {
            left *= 1.5;
            right *= 1.5;
        }

        return new DriveMessage((float) left, (float) right);
    }

    // Calculations used in calculateMovementForStick and calculateMovementForGamepad
    private Map<String, Double> generalStickCalculation(float x, float y) {
        float fPivYLimit = 22;

        double nMotPremixL;
        double nMotPremixR;
        double nPivSpeed;
        double fPivScale;

        if (y >= 0) {
            // Forward
            nMotPremixL = ((x >= 0) ? 127.0 : (127.0 + x));
            nMotPremixR = ((x >= 0) ? (127.0 - x) : 127.0);
        } else {
            // Reverse
            nMotPremixL = (x >= 0) ? (127.0 - x) : 127.0;
            nMotPremixR = (x >= 0) ? 127.0 : (127.0 + x);
        }

        nMotPremixL = nMotPremixL * y / 128.0;
        nMotPremixR = nMotPremixR * y / 128.0;

        nPivSpeed = x;
        fPivScale = (Math.abs(y) > fPivYLimit) ? 0.0 : (1.0 - Math.abs(y) / fPivYLimit);

        Map<String, Double> values = new HashMap<>();
        values.put("left", ((1.0 - fPivScale) * nMotPremixL + fPivScale * (-nPivSpeed)));
        values.put("right", ((1.0 - fPivScale) * nMotPremixR + fPivScale * nPivSpeed));
        return values;
    }


    // KeyEvent Handler
    @Override
    public void handle(KeyEvent event) {
        boolean changed = false;
        switch (event.getCode()) {
            case W:
                if (pressedW != (event.getEventType() == KeyEvent.KEY_PRESSED)) {
                    pressedW = event.getEventType() == KeyEvent.KEY_PRESSED;
                    changed = true;
                }
                break;
            case S:
                if (pressedS != (event.getEventType() == KeyEvent.KEY_PRESSED)) {
                    pressedS = event.getEventType() == KeyEvent.KEY_PRESSED;
                    changed = true;
                }
                break;
            case A:
                if (pressedA != (event.getEventType() == KeyEvent.KEY_PRESSED)) {
                    pressedA = event.getEventType() == KeyEvent.KEY_PRESSED;
                    changed = true;
                }
                break;
            case D:
                if (pressedD != (event.getEventType() == KeyEvent.KEY_PRESSED)) {
                    pressedD = event.getEventType() == KeyEvent.KEY_PRESSED;
                    changed = true;
                }
                break;
            case SPACE:
                if (pressedSpace != (event.getEventType() == KeyEvent.KEY_PRESSED)) {
                    pressedSpace = event.getEventType() == KeyEvent.KEY_PRESSED;
                    Connection.getInstance().send(new ShootMessage());
                }
                break;
            default:
                return;
        }
        if (changed) {
            Connection.getInstance().send(calculateMovementForKeyboard());
        }

    }

    public DriveMessage calculateMovementForKeyboard() {
        DriveMessage message = new DriveMessage(0, 0);
        if (pressedW) {
            message.increaseRightSpeed(100);
            message.increaseLeftSpeed(100);
        }
        if (pressedS) {
            message.increaseRightSpeed(-100);
            message.increaseLeftSpeed(-100);
        }

        if (pressedA) {
            message.increaseLeftSpeed(-50);
            if (pressedS) {
                message.increaseLeftSpeed(100);
            } else if (!pressedW) {
                message.increaseRightSpeed(50);
            }
        }
        if (pressedD) {
            message.increaseRightSpeed(-50);
            if (pressedS) {
                message.increaseRightSpeed(100);
            } else if (!pressedW) {
                message.increaseLeftSpeed(50);
            }
        }
        return message;
    }
}
