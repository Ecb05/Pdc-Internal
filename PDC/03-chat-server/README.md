# Experiment 03 — Chat Server (multi-client broadcast)

## Concept

A **chat server** is a standalone application with a **server** side and a **client**
side. It lets several users on a LAN exchange messages. To chat, a client first
connects to the server; the server then **broadcasts** every message to *all*
connected clients and keeps the list of active users in sync.

The design is the classic *thread-per-connection* server:

- The server runs an accept loop. Each accepted socket is handed to a dedicated
  **`ChatServerThread`**, so many clients are served concurrently.
- Each client runs a reader thread (`ChatClientThread`) that prints incoming messages
  while the main thread reads the console and sends what the user types.

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `java.net.ServerSocket` / `Socket` | Accept connections and talk to each client. |
| `java.lang.Thread` | One thread per connected client (server) + one reader thread per client. |
| `java.io.BufferedReader` / `InputStreamReader` | Read line-based text from the socket / console. |
| `java.io.PrintWriter` | Write line-based text to the socket (`println` + flush). |
| `java.util.List` | Server-side registry of connected clients for broadcasting. |

## Functionality

1. **Server** creates a `ServerSocket`, accepts clients in a loop, and stores each
   connected client in a shared list.
2. When a client sends a line, the server **broadcasts** `"Client <id>: <message>"` to
   every connected client.
3. On disconnect the client is removed from the list.
4. **Client** connects, spawns a reader thread that prints server messages, and sends
   each line the user types.

## Files

- `ChatServer.java` — accept loop, client registry, broadcast.
- `ChatServerThread.java` — per-client server thread.
- `ChatClient.java` — client entry point (console → server).
- `ChatClientThread.java` — client reader thread (server → console).

## Compile & run

```bash
javac ChatServer.java ChatServerThread.java ChatClient.java ChatClientThread.java

# Terminal 1
java ChatServer            # or: java ChatServer 8035

# Terminal 2, 3, ...
java ChatClient localhost 8035
```

## Notes / exam tips

- The original record used the deprecated `Thread.stop()`; the refactor uses a
  `volatile boolean running` flag and clean socket close instead.
- Each client is handled by its **own thread**, so one slow client cannot block the
  others.
- Broadcast writes are guarded by synchronizing on the shared client list.
