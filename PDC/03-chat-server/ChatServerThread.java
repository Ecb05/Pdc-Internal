import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * One server-side thread per connected chat client.
 *
 * Concept: reads lines from the client's socket and forwards them to the
 * server, which broadcasts them to everyone. The thread exits when the
 * client disconnects.
 */
public class ChatServerThread extends Thread {

    private final ChatServer server;
    private final Socket socket;
    private final int id;
    private PrintWriter out;

    public ChatServerThread(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.id = socket.getPort(); // unique-ish id from the client's port
    }

    public int getID() {
        return id;
    }

    /** Sends one line to this client. Safe to call from other threads. */
    public synchronized void send(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }

    @Override
    public void run() {
        System.out.println("Server thread " + id + " running");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase("quit")) {
                    break;
                }
                server.broadcast("Client " + id + ": " + line);
            }
        } catch (IOException e) {
            System.err.println("Client " + id + " error: " + e.getMessage());
        } finally {
            server.remove(id);
            try {
                socket.close();
            } catch (IOException ignored) {
                // already closing
            }
        }
    }
}
