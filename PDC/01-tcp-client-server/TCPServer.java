import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP server: listens on a port, accepts one client, and prints the UTF
 * string the client sends.
 *
 * Concept: TCP is a reliable, connection-oriented byte-stream protocol.
 * The ServerSocket binds the port (LISTEN state); accept() blocks until a
 * client performs the handshake, returning a connected Socket.
 */
public class TCPServer {

    /** Port the server listens on. Must match {@link TCPClient#PORT}. */
    public static final int PORT = 8034;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP server listening on port " + PORT + "...");

            // Blocks until a client connects.
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected: " + socket.getRemoteSocketAddress());

                DataInputStream in = new DataInputStream(socket.getInputStream());
                String message = in.readUTF(); // must pair with client's writeUTF()
                System.out.println("message = " + message);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
