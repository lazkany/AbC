/**
 * 
 */
package org.sysma.abc.core.ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Or;
import org.sysma.abc.core.topology.distributed.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Nondeter {
	public static class Process_1 extends AbCProcess {

		/**
		 * 
		 */
		public Process_1() {
			super();
			// TODO Auto-generated constructor stub
		}

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
			AbCPredicate subscribe = new Or(
					new HasValue("$1", this.getComponent().getStore().getValue("subscription1")),
					new HasValue("$2", this.getComponent().getStore().getValue("subscription1")));
			System.out.println("t" + this.id + " => received: " + receive(subscribe));

		}

	}

	public static class Process_2 extends AbCProcess {

		/**
		 * @param name
		 * @throws AbCAttributeTypeException
		 */
		public Process_2(String name) throws AbCAttributeTypeException {
			super(name);
			// TODO Auto-generated constructor stub
		}

		public Process_2() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			AbCPredicate subscribe = new HasValue("$1", this.getComponent().getStore().getValue("subscription3"));
			System.out.println("t" + this.id + " => received: " + receive(subscribe));

		}

	}

	public static class Process_3 extends AbCProcess {

		/**
		 * @param name
		 * @throws AbCAttributeTypeException
		 */

		public Process_3(String name) throws AbCAttributeTypeException {
			super(name);
			// TODO Auto-generated constructor stub
		}

		public Process_3() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {

			AbCPredicate p1 = new Or(new HasValue("$1", this.getComponent().getStore().getValue("subscription1")),
					new HasValue("$2", this.getComponent().getStore().getValue("subscription1")));
			AbCPredicate p2 = new HasValue("$1", this.getComponent().getStore().getValue("subscription3"));
			AbCPredicate p = new Or(p1, p2);
			
			
				Object xObject = receive(p);
				System.out.println(xObject);
//				if (p1.evaluate(xObject)) {
//					exec(new Process_1());
//				} else if (p2.evaluate(xObject)) {
//					exec(new Process_2());
//					exec(new Process_2());
//					//interrupt(2);
//				}
			
			

			// exec(new Process_2("t"));
			// interrupt(3);
			// exec(new Process_2());
			// }

		}
	}

	/**
	 * 
	 */
	public Nondeter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws AbCAttributeTypeException
	 * @throws AbCPortException 
	 */
	public static void main(String[] args) throws IOException, DuplicateNameException, AbCAttributeTypeException, AbCPortException {
		// TODO Auto-generated method stub
		System.out.println("Enter port number : ");
		int port = 0;
		int sig_port=0;
		int sub_port=0;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			port = Integer.parseInt(bufferRead.readLine());
			sig_port=Integer.parseInt(bufferRead.readLine());
			sub_port=Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port,sig_port);
		cPortClient.register( InetAddress.getLoopbackAddress() , sub_port );
		// Process_1 subscriber1 = new Process_1("subscriber_1");
		// Process_2 subscriber2 = new Process_2("subscriber_2");
		Process_3 main = new Process_3("main");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("subscription1", Object.class);
		Attribute<Object> a2 = new Attribute<Object>("subscription2", Object.class);
		Attribute<Object> a3 = new Attribute<Object>("subscription3", Object.class);
		store1.setValue(a1, "Movies");
		store1.setValue(a2, "News");
		store1.setValue(a3, "Songs");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(main);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
