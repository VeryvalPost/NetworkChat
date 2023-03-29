package Main.Server;


import org.junit.jupiter.api.*;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestServer {

    static int port = 5551;
    static String host = "localhost";
    Thread client;

    @BeforeEach
    public void threadStart() {
        System.out.println("Start tests.");

        client = new Thread(() -> {

            try {
                Socket connectSocket = new Socket(host, port);   // создание сокета подключения

                PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        client.start();
        client.setName("testThread");

    }

    @Test
    void serverConnectionTest() {
        boolean excepted = client.isAlive();
        System.out.println(client.getName());
        Assertions.assertTrue(excepted);
    }


    @AfterAll
    public static void afterTest() {
        System.out.println("All tests closed");
    }


}
