/**
 * 
 */
package org.sysma.abc.core.ex.GroupBased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.topology.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class GroupC {
	public static AbCPredicate FromgrpA = new HasValue("$1", "A");
	public static AbCPredicate ff=new FalsePredicate();
	public static HashMap<Attribute<?>, Object> update=new HashMap<>();;
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
			while(true){
			System.out.println(this.name + " => received: " + receive(FromgrpA) );
//			, new AbcUpdate() {
//
//				@Override
//				public Map<Attribute<?>, Object> eval(Object o) {
//					HashMap<Attribute<?>, Object> update = new HashMap<>();
//					update.put(getComponent().getStore().getAttribute("group"), o);
//					return update;
//				}})
//			
//				);
			}

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

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			// TODO Auto-generated method stub
			Thread.sleep(20000);
			send(ff, " ");
			setValue(new Attribute<String>("group",String.class), "B");

			System.out.println("joined group B");
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
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			port = Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port);
		cPortClient.register( InetAddress.getLoopbackAddress() , 9999 );
		Process_1 rcv = new Process_1("rcv_1");
		Process_2 join=new Process_2("joinA");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("group", Object.class);
		store1.setValue(a1, "C");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(rcv);
		c1.addProcess(join);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}