/**
 * 
 */
package org.sysma.abc.core.Examples;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.sysma.abc.core.centralized.ServerPort;
import org.sysma.abc.core.exceptions.DuplicateNameException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MainServer {

	/**
	 * 
	 */
	public MainServer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws DuplicateNameException 
	 */
	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub
		ServerPort srvr=new ServerPort();
		srvr.register("client_1", new InetSocketAddress(1234));
		srvr.register("client_2", new InetSocketAddress(1235));
		
	}

}
