import java.io.*;
import java.net.*;

public class Client {
    private InetAddress ClientIP;
    private final int SERVER_PORT = 1145; // Port sur lequel le serveur écoute

    public void handleConnexion() {
        try {
            Socket socket = new Socket("192.168.0.22", SERVER_PORT);
            System.out.println("Connected to server.");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
            Player player = new Player(0, 2);
            outObj.writeObject(player);

            System.out.print("Entrez votre nom d'utilisateur : ");
            String name_client = userInputReader.readLine();
            out.println(name_client);

            String userInput;

            Thread serverReaderThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = serverInputReader.readLine()) != null) {
                        System.out.println(serverMessage);
                        System.out.print(">>>");
                    }
                    socket.close();
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            });
            serverReaderThread.start();

            try {
                while ((userInput = userInputReader.readLine()) != null) {
                    System.out.print(">>>");
                    out.println(userInput);
                    if (userInput.equals("exit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.handleConnexion();
    }
}
