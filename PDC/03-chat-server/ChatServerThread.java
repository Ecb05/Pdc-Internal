import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public synchronized void send(String message) {
        out.println(message);
    }

    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
                ChatServer.broadcast("Client " + socket.getPort() + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            ChatServer.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                // Connection already closed.
            }
        }
    }
}
