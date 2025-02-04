package Server.src.main.java;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class MainServer {

    private static final List<Thread> threadList = new ArrayList<Thread>();
    private static final List<ClientHandler> clientList = new ArrayList<ClientHandler>();


    private static void StartMultiserver() throws IOException {
    // Creates new socket
    ServerSocket s = new ServerSocket(ClientHandler.PORT);

        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);
                ClientHandler r = new ClientHandler(socket);
                clientList.add(r);
                Thread task = new Thread(r);
                threadList.add(task);
                task.start();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        System.out.print('n');
    }
}
