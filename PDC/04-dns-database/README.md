# Experiment 04 — Domain Name Server (DNS) Implementation Using a Database

## Concept

The **Domain Name System (DNS)** is a hierarchical, distributed database that maps
human-readable **domain names** to **IP addresses**. It uses the client–server model:
the nodes of the database are **name servers**, and each domain has at least one
**authoritative** server. The client side is called a **resolver**, which sequences
queries until a name is fully resolved.

This experiment mimics that structure with three **authoritative servers**, one per
top-level domain, each backed by a MySQL table:

| Server | Port | Table |
|--------|------|--------|
| `DNSServer1` | 5123 | `root` |
| `DNSServer2` | 5124 | `yahoo` |
| `DNSServer3` | 5125 | `google` |

The resolver (`DNSClient`) splits a name like `www.google.com` into labels, contacts
the matching authoritative server, caches the answer in a `client` table, and builds
up the IP address label by label (right to left).

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `java.net.ServerSocket` / `Socket` | TCP channel between resolver and each name server. |
| `java.io.DataInputStream` / `BufferedReader` / `PrintStream` | Read the requested name and return port/IP/prefix lines. |
| `java.sql.DriverManager`, `Connection`, `PreparedStatement`, `ResultSet` | Talk to the MySQL database that stores the name→address records. |
| `com.mysql.cj.jdbc.Driver` | MySQL Connector/J JDBC driver. |
| `java.util.StringTokenizer` | Split a domain name into labels. |

## Functionality

1. Each authoritative server listens on its port, accepts a query string, extracts
   the final label (e.g. `google`), and looks it up in its table, returning
   **port**, **ipadd** and the remaining **prefix**.
2. The resolver repeatedly splits the name, checks its `client` cache, and if the
   label is unknown, opens a socket to the correct server port (starting at 5123),
   reads the answer, caches it, and continues with the prefix.
3. It accumulates the IP parts until only one label remains and prints the final IP.

## Files

- `DNSServer1.java`, `DNSServer2.java`, `DNSServer3.java` — authoritative servers.
- `DNSClient.java` — the resolver.
- `dns_schema.sql` — database/table setup with sample data.

## Database setup

```bash
mysql -u root < dns_schema.sql
```

Edit the JDBC URL/user/password constants in the sources if your MySQL port or
credentials differ.

## Compile & run

```bash
# Place the MySQL driver jar (e.g. mysql-connector-j-8.x.jar) in this folder.
export CLASSPATH=$CLASSPATH:./mysql-connector-j-8.x.jar

javac DNSServer1.java DNSServer2.java DNSServer3.java DNSClient.java

# Three terminals, one per authoritative server:
java DNSServer1
java DNSServer2
java DNSServer3

# Fourth terminal:
java DNSClient
# e.g. enter: www.google.com
```

## Notes / exam tips

- This uses the JDBC **4+** driver name `com.mysql.cj.jdbc.Driver`.
- The refactor replaces the string-concatenated SQL with `PreparedStatement`
  parameters (removes SQL-injection risk and handles quotes correctly).
- The resolver caches resolved labels in the `client` table, mirroring DNS caching.
