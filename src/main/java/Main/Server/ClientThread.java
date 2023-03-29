package Main.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {

    public Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    Scanner scanner = new Scanner(System.in);
    String name;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            setName();
            sendMsg(getName() + ", welcome to chat!");
            sendToAll(getName() + " connected to chat!");

            // создаем поток, ответственый за отправку сообщенией
            Thread sender = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    while (true) {
                        msg = scanner.nextLine();
                        sendToAll("Server message: " + msg);
                        LoggerClass.WriteMsg("Server: "+ msg);
                        out.flush();
                    }
                }
            });

            sender.start();

            // создаем поток, ответственый за приемку сообщенией
            Thread receive = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            String[] receiveMsg = msg.split(" ", 2);
                            sendToAll(receiveMsg[0] + " :" + receiveMsg[1]);   // принимая сообщение от кого-либо, сервер отправляет сообщение всем.
                            LoggerClass.WriteMsg(receiveMsg[0]+": "+ msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client disconnect");
                        out.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        LoggerClass.WriteMsg("Recieve msg error: " + e);
                    }

                }
            });
            receive.start();


        } catch (IOException e) {
            LoggerClass.WriteMsg("Client thread error: " + e);
        }
    }

    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            out.println(msg);
            out.flush();
        } catch (Exception e) {
            LoggerClass.WriteMsg("Send msg error: " + e);
        }
    }

    public void setName() throws IOException {
        name = in.readLine();
        out.flush();
    }

    public String getName() {
        return name;
    }

    public void sendToAll(String msg) {
        for (ClientThread o : Server.clients) {
            o.sendMsg(msg);
        }
    }
}