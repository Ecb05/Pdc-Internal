import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Client-side reader thread: listens for messages from the server and
 * forwards each one to ChatClient.handle().
 *
 * Concept: decoupling receiving from sending needs a second thread, since
 * readLine() blocks.
 */
public class ChatClientThread extends Thread {

    private final ChatClient client;
    private final Socket socket;

    public ChatClientThread(ChatClient client, Socket socket) {
        this.client = client;
        this.socket = socket;
        // Daemon so the client JVM can exit when the sending loop ends
        // (e.g. the user typed "quit" or stdin reached end-of-file).
        setDaemon(true);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                client.handle(line);
            }
        } catch (IOException e) {
            System.out.println("Listening stopped: " + e.getMessage());
        } finally {
            client.stop();
        }
    }
}
