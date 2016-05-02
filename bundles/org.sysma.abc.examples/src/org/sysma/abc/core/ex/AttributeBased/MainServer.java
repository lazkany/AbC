/**
 * 
 */
package org.sysma.abc.core.ex.AttributeBased;

import java.io.IOException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.AbCServer;
public class MainServer {
	public static void main(String[] args) throws IOException, DuplicateNameException {
		AbCServer srvr=new AbCServer();
		srvr.start();
	}
}
