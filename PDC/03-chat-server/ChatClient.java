import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Chat client.
 *
 * Concept: the main thread reads the console and sends each line to the
 * server; a separate ChatClientThread reads incoming messages and prints
 * them, so receiving never blocks typing.
 */
public class ChatClient implements Runnable {

    private final Socket socket;
    private final PrintWriter out;
    private volatile boolean running = true;

    public ChatClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected: " + socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        // Background reader: prints everything the server sends.
        new ChatClientThread(this, socket).start();

        // Foreground: read the user's console input and send it.
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (running && (line = console.readLine()) != null) {
                out.println(line);
                if (line.equalsIgnoreCase("quit")) {
                    stop();
                }
            }
        } catch (IOException e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    /** Called by the reader thread for each server message. */
    public void handle(String message) {
        System.out.println(message);
    }

    public void stop() {
        running = false;
        try {
            socket.close();
        } catch (IOException ignored) {
            // already closing
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ChatClient <host> <port>");
            return;
        }
        try {
            new ChatClient(args[0], Integer.parseInt(args[1])).run();
        } catch (IOException e) {
            System.err.println("Could not connect: " + e.getMessage());
        }
    }
}
