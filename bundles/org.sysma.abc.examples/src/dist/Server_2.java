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
public class Server_2 {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub

		System.out.println("SERVER_2");
		AbCServer srvr = new AbCServer(9991, 9990, 9989, 9988);

		System.out.println("Is subcribed to a perent server with a port 9997");

		srvr.registerToServer(InetAddress.getLoopbackAddress(), 9997);

		srvr.start();


	}
}
