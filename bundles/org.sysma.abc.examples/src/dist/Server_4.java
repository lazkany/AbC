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
public class Server_4 {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub

		System.out.println("SERVER_4");
		AbCServer srvr = new AbCServer(9983, 9982, 9981, 9980);

		System.out.println("Is subcribed to a perent server with a port 9993");

		srvr.registerToServer(InetAddress.getLoopbackAddress(), 9993);

		srvr.start();

	}
}
