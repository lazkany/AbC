/**
 * 
 */
package dist;

import java.io.IOException;
import java.net.InetAddress;

import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCServer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Server_1 {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub

		System.out.println("SERVER_1");
		AbCServer srvr = new AbCServer(9995, 9994, 9993, 9992);

		System.out.println("Is subcribed to a perent server with a port 9997");

		srvr.registerToServer(InetAddress.getLoopbackAddress(), 9997);

		srvr.start();

	}
}
