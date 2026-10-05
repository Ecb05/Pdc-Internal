# Experiment 01 — Client–Server Communication using TCP

## Concept

**TCP (Transmission Control Protocol)** lets two hosts exchange a *reliable, ordered
stream of bytes*. It runs on top of IP (which handles addressing and routing) and is
managed by the operating system through an **Internet socket** — the local endpoint of
a connection. TCP is **connection-oriented**: before data flows, a three-way handshake
(`SYN → SYN-ACK → ACK`) establishes the connection.

A TCP connection passes through states including `LISTEN`, `SYN-SENT`, `SYN-RECEIVED`,
`ESTABLISHED`, `FIN-WAIT-1/2`, `CLOSE-WAIT`, `CLOSING`, `LAST-ACK`, `TIME-WAIT` and
`CLOSED`. A connection is identified by the 4-tuple **(source host, source port,
destination host, destination port)**, which is why one server process can serve many
clients at once.

Role split:

- **Server** — creates a `ServerSocket`, binds a port, and blocks in `accept()` until a
  client connects.
- **Client** — creates a `Socket` that connects to the server's host + port.

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `java.net.ServerSocket` | Server-side socket that listens on a port and accepts connections (`accept()`). |
| `java.net.Socket` | Socket representing an established connection; gives input/output streams. |
| `java.io.DataInputStream` | Reads primitive/`UTF` data sent by the peer (`readUTF()`). |
| `java.io.DataOutputStream` | Writes primitive/`UTF` data to the peer (`writeUTF()`). |
| `java.net.InetSocketAddress` | (Under the hood) host + port pair used by `Socket`. |

## Functionality

1. `TCPServer` binds port **8034** and waits.
2. `TCPClient` connects to `127.0.0.1:8034` and writes the UTF string
   `"Hello Server from Client"`.
3. The server reads the string with `readUTF()` and prints it.
4. Both use try-with-resources so sockets/streams are always closed.

## Files

- `TCPServer.java` — the server.
- `TCPClient.java` — the client.

## Compile & run

```bash
javac TCPServer.java TCPClient.java

# Terminal 1
java TCPServer

# Terminal 2
java TCPClient
# Optional: java TCPClient <host>
```

## Notes / exam tips

- The **port numbers must match** between client and server.
- `readUTF()` / `writeUTF()` must be paired — use `readInt()`/`writeInt()` etc. for
  other types.
- `accept()` blocks until a client connects; the server then serves that one client.
  To serve many, loop over `accept()` and handle each socket on its own thread (see
  experiment 03).
