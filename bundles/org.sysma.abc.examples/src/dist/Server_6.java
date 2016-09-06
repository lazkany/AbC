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
public class Server_6 {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub

		System.out.println("SERVER_5");
		AbCServer srvr = new AbCServer(9975, 9974, 9973, 9972);

		System.out.println("Is subcribed to a perent server with a port 9989");

		srvr.registerToServer(InetAddress.getLoopbackAddress(), 9989);

		srvr.start();

	}
}
