import java.io.*;
import java.net.*;
import java.util.*;

public class Participant {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to coordinator.");

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    System.out.println("Coordinator disconnected.");
                    break;
                }

                if (message.startsWith("PREPARE ")) {
                    String transaction = message.substring(8);
                    System.out.println("Received PREPARE: " + transaction);
                    System.out.print("Vote YES or NO: ");

                    String vote = scanner.nextLine().trim();
                    while (!vote.equalsIgnoreCase("YES") && !vote.equalsIgnoreCase("NO")) {
                        System.out.print("Enter YES or NO: ");
                        vote = scanner.nextLine().trim();
                    }
                    out.println(vote.toUpperCase());
                } else if (message.equalsIgnoreCase("COMMIT")) {
                    System.out.println("Received COMMIT. Transaction committed.");
                    break;
                } else if (message.equalsIgnoreCase("ABORT")) {
                    System.out.println("Received ABORT. Transaction aborted.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
