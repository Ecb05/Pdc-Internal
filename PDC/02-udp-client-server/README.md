# Experiment 02 — Client–Server Communication using UDP

## Concept

**UDP (User Datagram Protocol)** is a **connectionless**, unreliable transport
protocol. Applications send independent **datagrams** to hosts on an IP network
*without* first setting up a channel — there is no handshake, no delivery guarantee,
no ordering and no duplicate protection. This makes UDP lightweight and well suited to
short request/response exchanges, broadcast and multicast, and protocols such as DNS.

Communication uses **datagram sockets**: each packet is individually addressed and
routed. Ports are 16-bit (0–65535) and are grouped as:

- `0–1023` — well-known services,
- `1024–49151` — registered ports,
- `49152–65535` — dynamic / ephemeral ports (often chosen by clients).

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `java.net.DatagramSocket` | Endpoint for sending/receiving datagrams. Server binds a port; client uses any free port. |
| `java.net.DatagramPacket` | A single datagram: byte buffer + (address, port) when sending. |
| `java.net.InetAddress` | Resolves a host name/IP (`getByName`) for the packet destination. |

## Functionality

1. `UDPServer` binds port **8057**, allocates a 1024-byte buffer, wraps it in a
   `DatagramPacket`, and blocks in `receive()`.
2. `UDPClient` builds the string `"IMPLEMENTATION OF UDP from Client"`, resolves the
   destination with `InetAddress.getByName("127.0.0.1")`, and calls `send()`.
3. The server prints the received bytes (decoded `0..getLength()` — the buffer may be
   larger than the payload).

## Files

- `UDPServer.java` — the receiver.
- `UDPClient.java` — the sender.

## Compile & run

```bash
javac UDPServer.java UDPClient.java

# Terminal 1
java UDPServer

# Terminal 2
java UDPClient
```

## Notes / exam tips

- With UDP the client does **not** connect; it just sends to `(host, port)`.
- A datagram may arrive out of order, be duplicated, or be lost — the application is
  responsible for any reliability it needs.
- Always decode using `packet.getLength()`; the buffer can be bigger than the message.
