package Server.src.main.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    public static final int PORT = 6666;
    public final BufferedReader in;
    public final PrintStream out;
    private static String clientMachine = "";
    private final Socket socket;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        clientMachine = this.socket.getInetAddress().getHostName();
        System.out.printf("%s connected.\n",clientMachine);
        out = new PrintStream(this.socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                this.socket.getInputStream()));
    }

    @Override
    public void run() {

    }

    private void closeQuietly(){
        try {
            System.out.println(String.format("%s has disconnected.\n",clientMachine));
            MainServer.getThreadList().remove(this);
            socket.close();
            in.close();
            out.close();
            MainServer.getClientList().remove(this);
        } catch (IOException e) {
        }
    }
}
