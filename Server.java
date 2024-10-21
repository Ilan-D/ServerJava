import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private List<ClientHandler> clients = new ArrayList<>();

    // est-ce que les threads sont comme differents terminaux ou je peux executer du
    // code en parallele ??
    public void start(int port) {
        try {
            InetAddress CurrentIP = InetAddress.getLocalHost();
            ServerSocket serverSocket = new ServerSocket(port, 10, CurrentIP);
            System.out.println("Server IP " + CurrentIP);
            System.out.println("Server started on port " + port);

            while (true) {

                Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket, this);

                clients.add(clientHandler);

                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ClientHandler> getClientsList() {
        return this.clients;
    }

    public void disconnectClient(ClientHandler client) {
        try {
            client.getSocket().close();
            clients.remove(client);
            System.out.println(client.getNameClient() + " disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BroadCastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(1145);
    }
}
