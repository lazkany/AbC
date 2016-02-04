/**
 * 
 */
package org.sysma.abc.core.Examples.PublishSubscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
//import org.sysma.abc.core.Examples.broadcast.broadcast.Process_1;
import org.sysma.abc.core.centralized.ServerPortAddress;
import org.sysma.abc.core.centralized.ServerPortClient;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Subscriber_1 {
	public static Attribute<Object> a1 = new Attribute<Object>("subscription", Object.class);
	public static class Process_1 extends AbCProcess {

		/**
		 * @param name
		 * @throws AbCAttributeTypeException 
		 */
		public Process_1(String name) throws AbCAttributeTypeException {
			super(name);
			// TODO Auto-generated constructor stub
			
			
		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {

			GroupPredicate subscription = new HasValue("$1", this.getComponent().getValue(a1));

			System.out.println(this.name + " => received: " + receive(subscription, null));

		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws AbCAttributeTypeException
	 */
	public static void main(String[] args) throws IOException, DuplicateNameException, AbCAttributeTypeException {
		// TODO Auto-generated method stub
		System.out.println("Enter port number : ");
		int port = 0;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			port = Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerPortClient cPortClient = new ServerPortClient(new ServerPortAddress(9998), new ServerSocket(port));
		cPortClient.RemoteRegister(new ServerPortAddress(9999));
		Process_1 brd1 = new Process_1("rcv_2");
		AbCEnvironment store1 = new AbCEnvironment();
		store1.setValue(a1, "Songs");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(brd1);
		c1.addPort(cPortClient);
		cPortClient.start();
		c1.start();
		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
