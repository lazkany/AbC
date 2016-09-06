/**
 * 
 */
package dist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCClient;
import org.sysma.abc.core.topology.distributed.AbCServer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MainServer {

	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub
		
		int ssub_port = 0;
		int sdata_port = 0;
		int csub_port = 0;
		int cdata_port = 0;
		int command_port = 0;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter server subscription port number : ");
			ssub_port = Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter server to server data port number : ");
			sdata_port = Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter client subscription port number : ");
			csub_port = Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter client data port number : ");
			cdata_port = Integer.parseInt(bufferRead.readLine());
			AbCServer srvr = new AbCServer(csub_port,cdata_port,ssub_port, sdata_port);
			System.out.println("subscription to parent?! " + "y/n");
			
			String command=bufferRead.readLine();
			if (command.equals("y")) {
				System.out.println("Please enter the command port");
				command_port = Integer.parseInt(bufferRead.readLine());
				srvr.registerToServer(InetAddress.getLoopbackAddress(), command_port);

			}
			srvr.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// AbCClient cPortClient = new
		// AbCClient(InetAddress.getLoopbackAddress(), port);
		// cPortClient.register( InetAddress.getLoopbackAddress() , 9999 );

	}

}
