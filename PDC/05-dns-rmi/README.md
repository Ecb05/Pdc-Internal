# Experiment 05 — DNS Implementation using Java RMI

## Concept

**Java RMI (Remote Method Invocation)** lets a program invoke a method on an object
that lives in **another address space** (another JVM, possibly on another machine). It
is essentially an object-oriented **RPC** mechanism.

Three processes participate:

1. **Client** — invokes a method on a remote object.
2. **Server** — owns the remote object; the object lives in the server's address space.
3. **Object Registry** — a name server relating objects to names. Objects are
   *registered* under a name; clients *look up* the name to obtain a reference.

### Stubs and skeletons

- A **stub** is a client-side proxy for the remote object; it forwards invocations
  over the network. The client's reference to a remote object is really a local stub.
- A **skeleton** is the server-side dispatcher that routes an incoming call to the
  actual remote object implementation.

### Steps to build an RMI application

1. Design the distributed components.
2. Define the **remote interface** (`extends Remote`).
3. Implement the remote object (`extends UnicastRemoteObject`).
4. Implement the client.
5. Compile sources (modern JDKs generate stubs dynamically — no `rmic` needed).
6. Make classes/registry reachable.
7. Run server, then client.

## APIs / classes involved

| API | Purpose |
|-----|---------|
| `java.rmi.Remote` | Marker interface every remote interface extends. |
| `java.rmi.RemoteException` | Checked exception for network failures in remote calls. |
| `java.rmi.server.UnicastRemoteObject` | Base class that exports the remote object and enables RMI. |
| `java.rmi.registry.LocateRegistry` | `createRegistry(port)` / `getRegistry(host, port)`. |
| `java.rmi.registry.Registry` | `bind`/`rebind` a name to an object; `lookup` to fetch a remote reference. |

## Functionality

- `DNSServerIntf` declares the remote method `DNS(String)`.
- `DNSServer` implements it as a small static lookup table of hostnames → IPs, then
  creates an RMI registry on port **8145** and binds itself under the name
  `"mvsrserver"`.
- `DNSClient` looks the object up and calls `DNS(...)`, printing the resolved IP.

## Files

- `DNSServerIntf.java` — remote interface.
- `DNSServer.java` — remote object + registry bootstrap.
- `DNSClient.java` — client.

## Compile & run

```bash
javac DNSServerIntf.java DNSServer.java DNSClient.java

# Terminal 1 — start server (creates the registry on 8145)
java DNSServer

# Terminal 2 — run client
java DNSClient www.osmania.ac.in
```

## Notes / exam tips

- The **remote interface must extend `Remote`** and every method must declare
  `throws RemoteException`.
- `rebind` overwrites an existing binding; `bind` fails if the name already exists.
- Modern Java (9+) no longer needs a security manager or `rmic`; the stub is created
  at runtime by `UnicastRemoteObject`.
