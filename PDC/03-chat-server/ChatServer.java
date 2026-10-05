import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Multi-client chat server.
 *
 * Concept: thread-per-connection server. The accept loop runs on the main
 * thread and hands each accepted socket to a dedicated ChatServerThread.
 * Every message received from any client is broadcast to all clients.
 */
public class ChatServer implements Runnable {

    public static final int DEFAULT_PORT = 8035;
    public static final int MAX_CLIENTS = 50;

    private final ServerSocket server;
    /** Guarded by synchronizing on this list. */
    private final List<ChatServerThread> clients = new ArrayList<>();
    private volatile boolean running = true;

    public ChatServer(int port) throws IOException {
        this.server = new ServerSocket(port);
        System.out.println("Chat server started on port " + port);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = server.accept();
                synchronized (clients) {
                    if (clients.size() >= MAX_CLIENTS) {
                        System.out.println("Client refused: maximum " + MAX_CLIENTS + " reached");
                        socket.close();
                        continue;
                    }
                    ChatServerThread client = new ChatServerThread(this, socket);
                    clients.add(client);
                    client.start();
                    System.out.println("Client accepted: " + socket.getRemoteSocketAddress());
                    broadcast("Server: client " + client.getID() + " joined the chat");
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Accept error: " + e.getMessage());
                }
            }
        }
    }

    /** Sends {@code message} to every connected client. */
    public void broadcast(String message) {
        synchronized (clients) {
            for (ChatServerThread client : clients) {
                client.send(message);
            }
        }
    }

    /** Removes a disconnected client from the registry. */
    public void remove(int id) {
        synchronized (clients) {
            clients.removeIf(client -> client.getID() == id);
        }
        System.out.println("Client " + id + " removed");
    }

    public void stop() {
        running = false;
        try {
            server.close();
        } catch (IOException ignored) {
            // shutting down anyway
        }
    }

    public static void main(String[] args) throws IOException {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        new ChatServer(port).run();
    }
}
