package Main.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;
        ExecutorService executeIt = Executors.newFixedThreadPool(200); //максимальное число пользователей ограничено
        Config conf = new Config();

        try {
            serverSocket = new ServerSocket(conf.PORT);
            System.out.println("Server starts on port: " + conf.PORT);
            LoggerClass.WriteMsg("Server starts on port: " + conf.PORT);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client accepted. Socket " + clientSocket.getPort());
                LoggerClass.WriteMsg("Client accepted. Socket " + clientSocket.getPort());
                ClientThread client = new ClientThread(clientSocket);
                clients.add(client);
                executeIt.execute(client);
                System.out.println(clients.toString());

            }

        } catch (IOException e) {
            LoggerClass.WriteMsg("Server error: " + e);
        }

    }
}

