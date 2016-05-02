/**
 * 
 */
package org.sysma.abc.core.ex.broadcast;

import java.io.IOException;

import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.AbCServer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MainServer {

	public static void main(String[] args) throws IOException, DuplicateNameException {
		// TODO Auto-generated method stub
		AbCServer srvr=new AbCServer();
		srvr.start();
	}

}
