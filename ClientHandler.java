import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    private String ClientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ClientName = in.readLine();
            System.out.println("Client connected : " + ClientName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // il faut appeler run et non start car le start du thread demarre le thread en
    // appelant la methode run.
    public void run() {
        try {
            // System.out.print("Veuillez entrer votre nom : ");
            // ClientName = in.readLine();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equals("exit")) {
                    server.disconnectClient(this);
                    break;
                }
                System.out.println(ClientName + " : " + clientMessage);
                server.BroadCastMessage(clientMessage, this);

                // Handle client message here
                // Example: Update game state, send updates to clients, etc.
            }

            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            Player player = (Player) inObj.readObject();
            System.out.println("x : " + player.getX() + " y : " + player.getY());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                server.getClientsList().remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getNameClient() {
        return this.ClientName;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
