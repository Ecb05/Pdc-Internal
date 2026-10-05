import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCP client: connects to the server and sends a single UTF string.
 *
 * Concept: opening a Socket performs the TCP handshake with the server;
 * the returned output stream writes bytes onto the ordered byte stream.
 */
public class TCPClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8034;
    public static final String MESSAGE = "Hello Server from Client";

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : HOST;

        try (Socket socket = new Socket(host, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(MESSAGE); // must pair with server's readUTF()
            out.flush();
            System.out.println("Sent to " + host + ":" + PORT + " -> " + MESSAGE);
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
