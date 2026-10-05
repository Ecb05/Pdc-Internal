import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP server: binds a port, receives one datagram, and prints its payload.
 *
 * Concept: UDP is connectionless and unreliable; each datagram is an
 * independent, individually addressed packet. The receiver uses a
 * DatagramSocket bound to a known port and reads bytes into a buffer.
 */
public class UDPServer {

    /** Port the server binds. Must match {@link UDPClient#PORT}. */
    public static final int PORT = 8057;
    public static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP server listening on port " + PORT + "...");

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet); // blocks until a datagram arrives

            // Decode only the bytes actually received (buffer may be larger).
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received from " + packet.getSocketAddress() + ": " + message);
        }
    }
}
