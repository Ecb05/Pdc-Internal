import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote object implementing the DNS lookup, plus the registry bootstrap.
 *
 * Concept: extending UnicastRemoteObject exports the object so its methods
 * can be invoked from other JVMs. It is then bound under a name in the RMI
 * registry, which clients use to look it up.
 */
public class DNSServer extends UnicastRemoteObject implements DNSServerIntf {

    private static final long serialVersionUID = 1L;

    public static final int RMI_PORT = 8145;
    public static final String BIND_NAME = "mvsrserver";

    public DNSServer() throws RemoteException {
        super();
    }

    @Override
    public String DNS(String hostname) throws RemoteException {
        switch (hostname) {
            case "www.osmania.ac.in":
                return "50.32.24.29";
            case "www.mvsrec.edu.in":
                return "90.82.44.89";
            case "www.jntu.ac.in":
                return "150.32.64.20";
            case "www.yahoo.com":
                return "88.39.124.129";
            default:
                return "No Info about this address";
        }
    }

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(RMI_PORT);
        registry.rebind(BIND_NAME, new DNSServer());
        System.out.println("server started...");
    }
}
