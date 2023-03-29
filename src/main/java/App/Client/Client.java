package App.Client;

import Main.Server.Config;




import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        Socket connectSocket;
        BufferedReader in;
        PrintWriter out;
        LoggerClass logClient = new LoggerClass();
        Config config = new Config();


// запускаем подключение сокета
        try {

            connectSocket = new Socket(config.HOST,config.PORT);   // создание сокета подключения
            out = new PrintWriter(connectSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
            logClient.WriteMsg("Connected to "+ connectSocket.getPort());

            Scanner scanner = new Scanner(System.in);    // создание сокета подключения
            System.out.println("Enter your name.");  // Инициализация имени клиента
            String name = scanner.nextLine();
            out.println(name);
            out.flush();

            // создаем поток, ответственый за отправку сообщенией
            Thread sender = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = scanner.nextLine();
                        if (msg.equals("/exit")){    // в случае, если клиент выходит их чата
                            out.close();
                            logClient.WriteMsg("Connection closed");
                        }

                        out.println(name + " " + msg);
                        logClient.WriteMsg("Output message:" + msg);
                        out.flush();



                    }
                }
            });

            sender.start();// стартуем поток, ответственый за отправку сообщенией

            // создаем поток, ответственый за прием сообщенией
            Thread receiver = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            String[] receiveMsg = msg.split(" ", 2);
                            if (!receiveMsg[0].equals(name)) {   // исключаем дублирование своих сообщений
                                System.out.println(msg);
                            }

                            msg = in.readLine();
                            logClient.WriteMsg("Input message from " + receiveMsg[0] + " :"+ receiveMsg[1]);
                        }
                        System.out.println("Server out of service"); // если сервер недоступен
                        in.close();
                        connectSocket.close();
                    } catch (IOException e) {
                        logClient.WriteMsg("Recieve msg error: " + e);
                    }

                }
            });
            receiver.start(); // стартуем поток, ответственый за прием сообщенией


        } catch (IOException e) {
            logClient.WriteMsg("Client connection error: " + e);
        }
    }
}