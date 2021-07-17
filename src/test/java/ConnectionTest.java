import ch.bbcag.mdriver.Constants;
import ch.bbcag.mdriver.common.MessageType;
import ch.bbcag.mdriver.common.StateCode;
import ch.bbcag.mdriver.common.StateMessage;
import ch.bbcag.mdriver.connection.Connection;
import org.junit.jupiter.api.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.MethodOrderer.Alphanumeric;

@TestMethodOrder(value = Alphanumeric.class)
public class ConnectionTest {

    ServerSocket serverSocket;

    final int testPort = 18999;

    @BeforeEach
    void createServer() {
        try {
            serverSocket = new ServerSocket(testPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void receiveTest() {
        new Thread(() -> { // Server thread
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(Constants.GSON.toJson(new StateMessage(StateCode.CONNECTION_OK)));
                dos.flush();
                Thread.sleep(200);
                dos.writeUTF(Constants.GSON.toJson(new StateMessage(StateCode.ABORT_RECORDING)));
                dos.flush();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Connection connection = Connection.getInstance("127.0.0.1", testPort, true);


        AtomicBoolean received = new AtomicBoolean(false);

        connection.addOnReceiveListener(message -> {
            Assertions.assertEquals(message.getType(), MessageType.STATE);

            StateMessage stateMessage = (StateMessage) message;

            Assertions.assertEquals(StateCode.ABORT_RECORDING, stateMessage.getStateCode());
            received.set(true);
        });
        while (!received.get()) ;
    }

    @Test
    public void establishedConnectionTest() {
        new Thread(() -> { // Server thread
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(Constants.GSON.toJson(new StateMessage(StateCode.CONNECTION_OK)));
                dos.flush();
                Thread.sleep(600);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Connection.getInstance("127.0.0.1", testPort, true);

        Assertions.assertTrue(Connection.isConnected());
    }

    @Test
    public void connectionWithoutServerTest() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection.getInstance("127.0.0.1", testPort, true);
        Assertions.assertFalse(Connection.isConnected());
    }

    @AfterEach
    void close() {
        try {
            if (serverSocket.isClosed()) return;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
