/**
 * 
 */
package org.sysma.abc.core.ex.PublishSubscribe;

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
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Or;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.distributed.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Subscriber_1 {
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
			//AbCPredicate subscribe = new Or(new HasValue("$1", this.getComponent().getStore().getValue("subscription")), new HasValue("$2", this.getComponent().getStore().getValue("subscription")));
			System.out.println(this.name + " => received: " + receive(o -> subscribe(o)));

		}
		public AbCPredicate subscribe( Object msg ) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(1).equals(this.getComponent().getStore().getValue("subscription"))||t.get(2).equals(this.getComponent().getStore().getValue("subscription"))) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
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
			System.out.println("Enter signal port number : ");
			sig_port=Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter subscribe port number : ");
			sub_port=Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port,sig_port);
		cPortClient.register( InetAddress.getLoopbackAddress() , sub_port );
		Process_1 subscriber1 = new Process_1("subscriber_1");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("subscription", Object.class);
		store1.setValue(a1, "Movies");
	
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(subscriber1);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
