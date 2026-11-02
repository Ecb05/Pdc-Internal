import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    static List<ChatServerThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8035);
        System.out.println("Server started on port 8035");

        while (true) {
            Socket socket = server.accept();
            ChatServerThread client = new ChatServerThread(socket);

            synchronized (ChatServer.class) {
                clients.add(client);
            }

            client.start();
        }
    }

    public static synchronized void broadcast(String message) {
        for (ChatServerThread client : clients) {
            client.send(message);
        }
    }

    public static synchronized void remove(ChatServerThread client) {
        clients.remove(client);
    }
}
