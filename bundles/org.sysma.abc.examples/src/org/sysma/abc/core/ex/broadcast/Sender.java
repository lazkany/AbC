/**
 * 
 */
package org.sysma.abc.core.ex.broadcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Sender {
	public static AbCPredicate any = new TruePredicate();

	public static class Process_1 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_1(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			// TODO Auto-generated method stub
			send(any, new Tuple("a","v"));
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws AbCAttributeTypeException
	 * @throws DuplicateNameException
	 * @throws AbCPortException 
	 */
	public static void main(String[] args) throws IOException, AbCAttributeTypeException, DuplicateNameException, AbCPortException {
		// TODO Auto-generated method stub
		System.out.println("Enter port number : ");
		int port = 0;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			port = Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port);
		cPortClient.register( InetAddress.getLoopbackAddress() , 9999 );
		AbCEnvironment store1 = new AbCEnvironment();
		AbCComponent c1 = new AbCComponent("C1", store1);
		Process_1 snd = new Process_1("snd_1");
		c1.addProcess(snd);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress());

	}

}
