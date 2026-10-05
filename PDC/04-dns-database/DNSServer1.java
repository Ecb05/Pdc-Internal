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
 * Authoritative DNS server for the "root" domain, listening on port 5123.
 *
 * Concept: mimics one node of the distributed DNS database. It receives a
 * requested name, takes its last label (e.g. "in" of www.osmania.ac.in),
 * and looks up the port/IP/canonical-prefix in the "root" table.
 */
public class DNSServer1 {

    static final int PORT = 5123;
    static final String DB_URL = "jdbc:mysql://localhost:3307/dns";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";
    static final String TABLE = "root";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("DNS Server1 (root) listening on port " + PORT);

            while (true) {
                try (Socket client = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintStream out = new PrintStream(client.getOutputStream())) {

                    String url = in.readLine();
                    System.out.println("IN SERVER1 URL IS: " + url);

                    // Split into dotted labels; keep everything except the last label.
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
