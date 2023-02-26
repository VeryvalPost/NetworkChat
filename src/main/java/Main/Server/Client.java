package Main.Server;


import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    final int id;
    final String name;
    final ClientThread clientThread;

    protected Client(int id, String name, ClientThread clientThread){
        this.id = id;
        this.name = name;
        this.clientThread = clientThread;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clientThread=" + clientThread +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }


}




