/**
 * 
 */
package dist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCServer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Root {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub
		System.out.println("The ROOT_SERVER");
		AbCServer srvr = new AbCServer(9999, 9998, 9997, 9996);
		srvr.start();

	}
}
