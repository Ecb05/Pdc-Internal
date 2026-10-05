import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP client: sends a single datagram to the server.
 *
 * Concept: the client does not connect — it wraps the payload with the
 * destination address and port inside a DatagramPacket and calls send().
 */
public class UDPClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8057;
    public static final String MESSAGE = "IMPLEMENTATION OF UDP from Client";

    public static void main(String[] args) throws Exception {
        String host = (args.length > 0) ? args[0] : HOST;
        byte[] data = MESSAGE.getBytes();

        // A client socket needs no fixed port — the OS assigns an ephemeral one.
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
            socket.send(packet);
            System.out.println("Sent to " + host + ":" + PORT + " -> " + MESSAGE);
        }
    }
}
