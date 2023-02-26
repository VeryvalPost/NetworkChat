package Main.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    static ExecutorService executeIt = Executors.newFixedThreadPool(200); //максимальное число пользователей ограничено
    private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();


    static int port = 8080;

    public Server() throws IOException {

        // стартуем сервер на порту
        ServerSocket server = new ServerSocket(port);

        try {
            System.out.println("Server starts at port: " + port);

            // бесконечный цикл для обработки всех поступающих запросов
            while (true) {
                Socket newClient = server.accept();
                // получаем запрос от клиента и создаем отдельный runnable поток
                ClientThread client = new ClientThread(newClient, this);
                clients.add(client);
                executeIt.execute(client);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // отправляем сообщение всем клиентам
    public void sendMessageToAllClients(String msg) {
        for (ClientThread o : clients) {
            o.sendMsg(msg);
        }

    }

    // удаляем клиента из коллекции при выходе из чата
    public void removeClient(ClientThread client) {
        clients.remove(client);
    }
}