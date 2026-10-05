import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI client: looks up the remote DNS object in the registry and calls it.
 *
 * Concept: the client never sees the server object directly — it obtains a
 * local stub from the registry and invokes methods on that stub, which
 * forwards the call over the network.
 */
public class DNSClient {

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", DNSServer.RMI_PORT);
        DNSServerIntf service = (DNSServerIntf) registry.lookup(DNSServer.BIND_NAME);

        String hostname = (args.length > 0) ? args[0] : "www.osmania.ac.in";
        System.out.println("The website name is: " + hostname);
        System.out.println("The IP Address is: " + service.DNS(hostname));
    }
}
