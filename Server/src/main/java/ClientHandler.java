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

        String responseMessage = connection.getResponseMessage();

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Code: " + responseMessage);
        out = new PrintStream(this.socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String messageFromClient;

            // Main command loop
            while ((messageFromClient = in.readLine()) != null) {
                // Converts message received from client to a JSONObject
                      try {
            String messageFromClient;

            // Main command loop
            while ((messageFromClient = in.readLine()) != null) {
                // Converts message received from client to a JSONObject
                JSONObject clientRequest = stringToJSON(messageFromClient);

                //Conditional block to execute launch
                if (!clientRequest.isEmpty() &&
                        clientRequest.get("command").equals("launch") &&
                        (!this.isLaunched() || this.robot.getStatus().equals("DEAD")))
                {
                    try {
                        ClientCommand clientCommand = ClientCommand.create(clientRequest);
                        out.println(clientCommand.getResponse(this));
                        MainServer.pushUpdatedWorld();
                    } catch (RequestException e) {
                        out.println(Response.getErrorResponse(e.getMessage()));
                    }
                    continue;
                }

                // Checks if client has launched a robot to do perform other commands
                else if (!clientRequest.isEmpty() &&
                        this.isLaunched())
                {
                    try {
                        ClientCommand clientCommand = ClientCommand.create(clientRequest);
                        switch (this.robot.getStatus()) {
                            case "NORMAL":
                                out.println(clientCommand.getResponse(this));
                                MainServer.pushUpdatedWorld();
                                break;
                            case "RELOAD":
                                out.println(Response.getErrorResponse("Robot is currently reloading..."));
                                break;
                            case "REPAIR":
                                out.println(Response.getErrorResponse("Robot is currently repairing shields..."));
                                break;
                            case "DEAD":
                                out.println(Response.getErrorResponse("You have been killed, please launch again to continue"));
                                break;
                            default:
                                out.println(Response.getErrorResponse("Robot state cannot be identified"));
                        }
                    } catch (RequestException e) {
                        out.println(Response.getErrorResponse(e.getMessage()));
                    }
                }

                // Responds with error message if command attempt was made before launching
                else if (!clientRequest.isEmpty()) {
                    out.println(Response.getErrorResponse("Please launch Robot before attempting any commands"));
                }
            }
        } catch (IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }bject clientRequest = stringToJSON(messageFromClient);

                //Conditional block to execute launch
                if (!clientRequest.isEmpty() &&
                        clientRequest.get("command").equals("launch") &&
                        (!this.isLaunched() || this.robot.getStatus().equals("DEAD")))
                {
                    try {
                        ClientCommand clientCommand = ClientCommand.create(clientRequest);
                        out.println(clientCommand.getResponse(this));
                        MainServer.pushUpdatedWorld();
                    } catch (RequestException e) {
                        out.println(Response.getErrorResponse(e.getMessage()));
                    }
                    continue;
                }

                // Checks if client has launched a robot to do perform other commands
                else if (!clientRequest.isEmpty() &&
                        this.isLaunched())
                {
                    try {
                        ClientCommand clientCommand = ClientCommand.create(clientRequest);
                        switch (this.robot.getStatus()) {
                            case "NORMAL":
                                out.println(clientCommand.getResponse(this));
                                MainServer.pushUpdatedWorld();
                                break;
                            case "RELOAD":
                                out.println(Response.getErrorResponse("Robot is currently reloading..."));
                                break;
                            case "REPAIR":
                                out.println(Response.getErrorResponse("Robot is currently repairing shields..."));
                                break;
                            case "DEAD":
                                out.println(Response.getErrorResponse("You have been killed, please launch again to continue"));
                                break;
                            default:
                                out.println(Response.getErrorResponse("Robot state cannot be identified"));
                        }
                    } catch (RequestException e) {
                        out.println(Response.getErrorResponse(e.getMessage()));
                    }
                }

                // Responds with error message if command attempt was made before launching
                else if (!clientRequest.isEmpty()) {
                    out.println(Response.getErrorResponse("Please launch Robot before attempting any commands"));
                }
            }
        } catch (IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }


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
