import java.io.*;
import java.net.*;
import java.util.*;

public class Coordinator {
    private static final int PORT = 5000;
    private static final List<Socket> participants = new ArrayList<>();
    private static final List<PrintWriter> outstreams = new ArrayList<>();
    private static final List<BufferedReader> instreams = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of participants: ");
        int numParticipants = scanner.nextInt();
        scanner.nextLine();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Waiting for " + numParticipants + " participants...");

            for (int i = 0; i < numParticipants; i++) {
                Socket socket = serverSocket.accept();
                participants.add(socket);
                outstreams.add(new PrintWriter(socket.getOutputStream(), true));
                instreams.add(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                System.out.println("Participant " + (i + 1) + " connected.");
            }

            System.out.print("Enter transaction details: ");
            String transaction = scanner.nextLine();
            boolean success = initiateTwoPhaseCommit(transaction);

            if (success)
                System.out.println("Transaction committed successfully.");
            else
                System.out.println("Transaction aborted.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            closeConnections();
            scanner.close();
        }
    }

    private static boolean initiateTwoPhaseCommit(String transaction) {
        try {
            System.out.println("Sending PREPARE to all participants...");
            for (PrintWriter out : outstreams)
                out.println("PREPARE " + transaction);

            List<String> votes = new ArrayList<>();
            for (BufferedReader in : instreams) {
                String vote = in.readLine();
                votes.add(vote == null ? "NO" : vote.trim());
            }

            System.out.println("Collected votes: " + votes);
            boolean allAgreed = votes.stream().allMatch(vote -> vote.equalsIgnoreCase("YES"));

            if (allAgreed) {
                System.out.println("All participants voted YES. Sending COMMIT...");
                for (PrintWriter out : outstreams)
                    out.println("COMMIT");
                return true;
            } else {
                System.out.println("At least one participant voted NO. Sending ABORT...");
                for (PrintWriter out : outstreams)
                    out.println("ABORT");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error during commit: " + e.getMessage());
            for (PrintWriter out : outstreams)
                out.println("ABORT");
            return false;
        }
    }

    private static void closeConnections() {
        for (Socket socket : participants) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
