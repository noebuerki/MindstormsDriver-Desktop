package ch.bbcag.mdriver.input;

import ch.bbcag.mdriver.navigation.Navigator;
import ch.bbcag.mdriver.navigation.SceneType;
import javafx.application.Platform;
import net.java.games.input.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ControllerManager implements Runnable {

    private static float DEAD_ZONE;
    private static float REST_DEAD_ZONE;
    private final Navigator navigator;
    private final AtomicInteger controllerCounter = new AtomicInteger();
    private ControllerListener controllerListener;
    private Controller controller;
    private Map<Component.Identifier, Float> componentValues;
    private Map<Component.Identifier, Float> lastValues;
    private boolean isRunning;
    private boolean shouldStop;
    private Thread environmentCreateThread;

    public ControllerManager(Navigator navigator) {
        this.navigator = navigator;
        controllerCounter.set(getAllControllers().size());
    }

    @SuppressWarnings("unchecked")
    private static ControllerEnvironment createControllerEnvironment() {
        try {
            Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>) Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Controller> getAllControllers() {
        List<Controller> filteredDevices = new ArrayList<>();
        Controller[] devices = Objects.requireNonNull(createControllerEnvironment()).getControllers();
        for (Controller device : devices) {
            Controller.Type type = device.getType();
            if (Controller.Type.STICK.equals(type) ||
                    Controller.Type.GAMEPAD.equals(type)) {
                filteredDevices.add(device);
            }
        }
        return filteredDevices;
    }

    @Override
    public void run() {
        while (!shouldStop) {
            if (isRunning) {
                checkControllerPlugin();

                if (controller != null) {
                    handleControllerPoll();
                }
            }
        }
    }

    public void setupStick() {
        DEAD_ZONE = 0.045f;
        REST_DEAD_ZONE = 0.005f;
        componentValues.put(Component.Identifier.Axis.X, 0f);
        componentValues.put(Component.Identifier.Axis.Y, 0f);
        componentValues.put(Component.Identifier.Axis.RZ, 0f);
        componentValues.put(Component.Identifier.Button._1, 0f);
    }

    public void setupGamepad() {
        DEAD_ZONE = 0.1f;
        REST_DEAD_ZONE = 0.05f;
        componentValues.put(Component.Identifier.Axis.X, 0f);
        componentValues.put(Component.Identifier.Axis.RX, 0f);
        componentValues.put(Component.Identifier.Axis.RY, 0f);
        componentValues.put(Component.Identifier.Button._1, 0f);
    }

    private void handleControllerPoll() {
        if (controller.poll()) {
            EventQueue queue = controller.getEventQueue();

            Event event = new Event();
            while (queue.getNextEvent(event)) {
                if (componentValues.containsKey(event.getComponent().getIdentifier())) {
                    Float lastValue = lastValues.get(event.getComponent().getIdentifier());
                    float newValue = event.getComponent().getPollData();
                    float div;
                    if (lastValue != null) {
                        div = Math.abs(lastValue - newValue);
                    } else {
                        div = Math.abs(newValue);
                    }
                    if (div > DEAD_ZONE || (newValue < REST_DEAD_ZONE && div > REST_DEAD_ZONE)) {
                        controllerListener.onInput(event);
                        lastValues.put(event.getComponent().getIdentifier(), newValue);
                    }
                }
            }
        } else {
            onPollFailed();
        }
    }

    private void onPollFailed() {
        controller = null;
    }

    private void checkControllerPlugin() {
        if (environmentCreateThread == null || !environmentCreateThread.isAlive()) {
            environmentCreateThread = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Controller> controllers = ControllerManager.getAllControllers();
                if (controllers.size() != controllerCounter.get()) {
                    if (controllers.size() == 1) {
                        setController(controllers.get(0));
                    } else if (controllers.size() > 1) {
                        isRunning = false;
                        controller = null;
                        Platform.runLater(() -> navigator.navigateTo(SceneType.CONTROLLER_SELECT));
                    } else {
                        controller = null;
                    }
                    controllerCounter.set(controllers.size());
                }
            });
            environmentCreateThread.start();
        }
    }


    public boolean isRunning() {
        return isRunning;
    }

    public void isRunning(Boolean running) {
        isRunning = running;
    }

    public void shouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        lastValues = new HashMap<>();
        componentValues = new HashMap<>();
        if (controller.getType().equals(Controller.Type.GAMEPAD) ||
                controller.getName().toLowerCase(Locale.ROOT).contains("controller") ||
                controller.getName().toLowerCase(Locale.ROOT).contains("gamepad")) {
            setupGamepad();
        } else if (controller.getType().equals(Controller.Type.STICK)) {
            setupStick();
        }
        this.controller = controller;
    }

    public void setControllerListener(ControllerListener controllerListener) {
        this.controllerListener = controllerListener;
    }

    public Map<Component.Identifier, Float> getComponentValues() {
        return componentValues;
    }
}
