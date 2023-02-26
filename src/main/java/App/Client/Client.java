package App.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

       public static void main(String[] args) throws InterruptedException {

// запускаем подключение сокета
        try(Socket connectSocket = new Socket("localhost",8080);
            PrintWriter toServer = new PrintWriter(connectSocket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(connectSocket.getInputStream())))
        {
            System.out.println("Please enter your name: ");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            toServer.write(name+"\n");
            toServer.flush();
            System.out.println("Client connected to socket: "+connectSocket.getLocalSocketAddress());

            while(!connectSocket.isClosed()) {
                String messageToChat = scanner.nextLine();
                toServer.write(messageToChat);
                toServer.flush();
            }

            while(!connectSocket.isClosed()) {
                try {
                    String message = fromServer.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}