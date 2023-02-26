package Main.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ClientThread implements Runnable {

    private Server server;
    private Socket newClient;
    private PrintWriter toClient;
    private BufferedReader fromClient;


    public ClientThread(Socket newClient, Server server) {

        try {
            this.newClient = newClient;
            this.server = server;
            // запуск каналов передачи данных клиентского сокета
            this.toClient = new PrintWriter(newClient.getOutputStream(), true);
            this.fromClient = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        @Override
        public void run() {

            try {
                // инициализация нового клиента
                System.out.println("Connected user at " + newClient.getRemoteSocketAddress());
                String name = fromClient.readLine();
                System.out.println("Name of user: " + name);
                toClient.flush();
                toClient.print("You enter the chat. To end the conversation, type \"/exit\"\n");
                System.out.println("You enter the chat: " + name);
                System.out.println("можно писать");
                Scanner scanner = new Scanner(System.in);
                String serverMessage = scanner.nextLine();
                toClient.write(serverMessage);
                toClient.flush();
                toClient.print(serverMessage+ "print");
                toClient.flush();

                // постоянная работа с клиентом в цикле чтение
                while (true) {
                    if (fromClient.ready()) {
                        String clientMessage = fromClient.readLine();

                        if (clientMessage.equalsIgnoreCase("\"/exit\"")) {
                            break;
                        }

                        // выводим в консоль сообщение (для теста)
                        System.out.println(clientMessage);
                        // отправляем данное сообщение всем клиентам
                        server.sendMessageToAllClients(clientMessage);
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            toClient.print(msg+"\n");
            toClient.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}