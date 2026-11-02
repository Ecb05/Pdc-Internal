# Experiment 03 — Multi-Client Chat Server

## TL;DR: How the application works

The server accepts multiple TCP clients. Each client gets its own server thread. When a client sends a message, the server broadcasts it to every connected client.

## Purpose of each file

- **ChatServer.java** — Starts the server on port 8035, accepts client connections, stores the connected client handlers, and broadcasts messages to all clients.
- **ChatServerThread.java** — One object/thread per connected client. It reads messages from that client, forwards normal messages to ChatServer.broadcast(), and removes the client when it disconnects or types `quit`.
- **ChatClient.java** — Connects to the server, reads messages typed by the user, and sends them to the server.
- **ChatClientThread.java** — Runs in the background on each client. It receives messages from the server and prints them while the main client thread is waiting for keyboard input.

## Basic message flow

1. ChatServer waits for a connection using `accept()`.
2. When a client connects, ChatServer creates and starts a ChatServerThread for that socket.
3. ChatClient sends typed messages through its socket.
4. ChatServerThread reads the message and calls `ChatServer.broadcast()`.
5. ChatServer.broadcast() calls `send()` on every connected client handler.
6. Each ChatClientThread receives and prints the broadcast message.
7. Typing `quit` disconnects that client.

## Compile

Run these commands from this folder:

```bash
javac ChatServer.java ChatServerThread.java ChatClient.java ChatClientThread.java
```

## Run

Open separate terminals.

**Terminal 1 — Start the server:**

```bash
java ChatServer
```

**Terminal 2 and any additional terminals — Start clients:**

```bash
java ChatClient
```

Type messages in any client terminal. Type `quit` to disconnect that client.

## Main concepts

- **ServerSocket:** Listens for incoming connections.
- **Socket:** Represents a TCP connection between a client and server.
- **Thread:** Lets the server handle clients concurrently.
- **BufferedReader:** Reads incoming text line by line.
- **PrintWriter:** Sends text through a socket.
- **broadcast():** Sends one message to all connected clients.
