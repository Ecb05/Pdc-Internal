import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 * Authoritative DNS server for the "google" domain, listening on port 5125.
 * See {@link DNSServer1} for the shared logic.
 */
public class DNSServer3 {

    static final int PORT = 5125;
    static final String DB_URL = "jdbc:mysql://localhost:3307/dns";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";
    static final String TABLE = "google";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("DNS Server3 (google) listening on port " + PORT);

            while (true) {
                try (Socket client = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintStream out = new PrintStream(client.getOutputStream())) {

                    String url = in.readLine();
                    System.out.println("IN SERVER3 URL IS: " + url);

                    StringBuilder prefix = new StringBuilder();
                    StringTokenizer st = new StringTokenizer(url, ".");
                    while (st.countTokens() > 1) {
                        prefix.append(st.nextToken()).append(".");
                    }
                    String name = st.nextToken();

                    PreparedStatement ps = con.prepareStatement(
                            "SELECT port, ipadd FROM " + TABLE + " WHERE name = ?");
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        out.println(rs.getString("port"));
                        out.println(rs.getString("ipadd"));
                        out.println(prefix.toString());
                    } else {
                        out.println("Illegal address, please check the spelling again");
                    }
                }
            }
        }
    }
}
