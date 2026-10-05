import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 * DNS resolver.
 *
 * Concept: resolves a name from right to left. For each unknown label it
 * queries the matching authoritative server (starting at port 5123), caches
 * the answer in the "client" table, and accumulates the IP address parts.
 */
public class DNSClient {

    static final String DB_URL = "jdbc:mysql://localhost:3307/dns";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";
    static final int FIRST_SERVER_PORT = 5123;

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter name to resolve:");
        String url = console.readLine();

        String ip = "";
        boolean status = true;
        int pno = FIRST_SERVER_PORT;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            while (status) {
                System.out.println("IN CLIENT URL IS: " + url);

                StringTokenizer st = new StringTokenizer(url, ".");
                StringBuilder prefix = new StringBuilder();
                if (st.countTokens() == 1) {
                    status = false;
                }
                while (st.countTokens() > 1) {
                    prefix.append(st.nextToken()).append(".");
                }
                String name = st.nextToken();
                System.out.println("u=" + name);

                // 1. Try the local cache table first.
                PreparedStatement cache = con.prepareStatement(
                        "SELECT port, ipadd FROM client WHERE name = ?");
                cache.setString(1, name);
                ResultSet rs = cache.executeQuery();

                if (rs.next()) {
                    pno = Integer.parseInt(rs.getString("port"));
                    ip = rs.getString("ipadd") + "." + ip;
                } else {
                    // 2. Ask the authoritative server on the current port.
                    System.out.println("pno=" + pno);
                    try (Socket socket = new Socket("127.0.0.1", pno);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintStream out = new PrintStream(socket.getOutputStream())) {

                        out.println(url);
                        String portStr = in.readLine();
                        String ipPart = in.readLine();
                        String prefixStr = in.readLine();

                        pno = Integer.parseInt(portStr);
                        url = prefixStr;                 // continue with the parent domain
                        ip = ipPart + "." + ip;

                        // 3. Cache the answer for next time.
                        PreparedStatement insert = con.prepareStatement(
                                "INSERT INTO client VALUES (?, ?, ?)");
                        insert.setString(1, name);
                        insert.setString(2, ipPart);
                        insert.setString(3, portStr);
                        insert.executeUpdate();
                    }
                }
                System.out.println("ip=" + ip);
            }
            ip = ip.substring(0, ip.length() - 1).trim();
            System.out.println("IP address is: " + ip);
        }
    }
}
