import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8035);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Separate thread receives messages while this thread reads keyboard input.
        new ChatClientThread(socket).start();

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Connected. Type messages (type quit to exit):");

        String message;
        while ((message = console.readLine()) != null) {
            out.println(message);
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
        }

        socket.close();
        console.close();
    }
}
