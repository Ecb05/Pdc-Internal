import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the DNS service.
 *
 * Concept: every remote interface must extend java.rmi.Remote, and each
 * method must declare that it throws RemoteException.
 */
public interface DNSServerIntf extends Remote {

    /** Returns the IP address for the given hostname. */
    String DNS(String hostname) throws RemoteException;
}
