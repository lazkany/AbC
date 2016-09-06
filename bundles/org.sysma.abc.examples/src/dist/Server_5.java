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
public class Server_5 {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub

		System.out.println("SERVER_5");
		AbCServer srvr = new AbCServer(9979, 9978, 9977, 9976);

		System.out.println("Is subcribed to a perent server with a port 9989");

		srvr.registerToServer(InetAddress.getLoopbackAddress(), 9989);

		srvr.start();

	}
}
