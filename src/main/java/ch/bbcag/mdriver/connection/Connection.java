package ch.bbcag.mdriver.connection;

import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.Message;
import ch.bbcag.mdriver.common.StateCode;
import ch.bbcag.mdriver.common.StateMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class Connection {

    private static Connection instance;

    private static String host;
    private static Integer port;
    private static boolean isConnected;

    private final Set<SendListener> sendListeners = new HashSet<>();
    private final Set<ReceiveListener> receiveListeners = new HashSet<>();
    private boolean shouldReconnect = true;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;


    private Connection(String host, int port) {
        connect(host, port);
        addOnReceiveListener(new ClosingServerManager());
    }

    public static Connection getInstance() {
        if (instance == null) {
            if (host == null && port == null) {
                throw new InvalidParameterException("You must call getInstance(String, int) first");
            } else {
                instance = new Connection(host, port);
            }
        }
        return instance;
    }

    public static Connection getInstance(String host, int port) {
        if (instance == null || !isConnected) {
            instance = new Connection(host, port);
        }
        return instance;
    }

    public static Connection getInstance(String host, int port, boolean overWrite) {
        if (overWrite) {
            instance = new Connection(host, port);
        }
        return instance;
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void isConnected(boolean connected) {
        Connection.isConnected = connected;
    }

    public void shouldReconnect(boolean shouldReconnect) {
        this.shouldReconnect = shouldReconnect;
    }

    private void connect(String host, int port) {
        try {
            socket = new Socket();
            Connection.host = host;
            Connection.port = port;
            socket.connect(new InetSocketAddress(Connection.host, Connection.port), 1000);

            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            try {
                sleep(100);
            } catch (InterruptedException | IllegalMonitorStateException e) {
                e.printStackTrace();
            }
            Message message = receive();
            if (message instanceof StateMessage) {
                if (((StateMessage) message).getStateCode() == StateCode.CONNECTION_OK) {
                    isConnected = true;
                    startListening();
                }
            }
        } catch (IOException e) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    private void reconnect() {
        while (!isConnected && shouldReconnect) {
            connect(host, port);
        }
    }

    public void send(Message message) {
        if (isConnected) {
            String json = Constants.GSON.toJson(message);
            for (SendListener sendListener : sendListeners) {
                if (sendListener != null) {
                    sendListener.onSend(message);
                }
            }
            try {
                dos.writeUTF(json);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                isConnected = false;
            }
        }
    }

    public void addOnSendListener(SendListener listener) {
        sendListeners.add(listener);
    }

    private Message receive() {
        try {
            return Constants.GSON.fromJson(dis.readUTF(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOnReceiveListener(ReceiveListener listener) {
        receiveListeners.add(listener);
    }

    public void removeOnReceiveListener(ReceiveListener listener) {
        receiveListeners.remove(listener);
    }

    public void startListening() {
        new Thread(() -> {
            while (isConnected) {
                try {
                    String message = dis.readUTF();
                    for (ReceiveListener listener : receiveListeners) {
                        listener.onReceive(Constants.GSON.fromJson(message, Message.class));
                    }
                } catch (IOException e) {
                    isConnected = false;
                    reconnect();
                    break;
                }
            }
        }).start();
    }
}
