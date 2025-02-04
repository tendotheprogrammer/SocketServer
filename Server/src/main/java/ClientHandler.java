package Server.src.main.java;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;


public class ClientHandler implements Runnable {

    public static final int PORT = 6666;
    public final BufferedReader in;
    public final PrintStream out;
    private static String clientMachine = "";
    private final Socket socket;
    HttpURLConnection connection;
    URL url = new URL("wss://api.play.ai/v1/talk/sawwwaw-cJJv5dLwfSwdNXY4dS6f8");


    //const myWs = new WebSocket('wss://api.play.ai/v1/talk/sawwwaw-cJJv5dLwfSwdNXY4dS6f8');
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        clientMachine = this.socket.getInetAddress().getHostName();
        System.out.printf("%s connected.\n",clientMachine);
        connection = (HttpURLConnection) url.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Enable input and output streams for sending and receiving data
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // Set the content type to application/json
        connection.setRequestProperty("Content-Type", "application/json");

        // Create the POST body data
        String jsonInputString = "{ \"type\": \"setup\", \"apiKey\": \"ak-7a691c95bcbc4dc19ce07c30e5f177d3\" }";

        // Write the JSON input to the output stream
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.writeBytes(jsonInputString);
            writer.flush();
        }

        // Get the response code (200 is OK, 400 is bad request, etc.)
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
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
