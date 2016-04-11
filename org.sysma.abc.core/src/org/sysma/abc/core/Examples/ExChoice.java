/**
 * 
 */
package org.sysma.abc.core.Examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.NonDeterminism;
import org.sysma.abc.core.Examples.Nondeter.Process_3;
import org.sysma.abc.core.centralized.ServerPortAddress;
import org.sysma.abc.core.centralized.ServerPortClient;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;
import org.sysma.abc.core.grpPredicate.Or;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class ExChoice {

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
//			GroupPredicate subscribe = new Or(
//					new HasValue("$1", this.getComponent().getStore().getValue("subscription1")),
//					new HasValue("$2", this.getComponent().getStore().getValue("subscription1")));
			System.out.println("t" + this.id + " => received: " + getRecValue());

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
//			GroupPredicate subscribe = new HasValue("$1", this.getComponent().getStore().getValue("subscription3"));
			System.out.println("t" + this.id + " => received: " + getRecValue());

		}

	}

	public static class Process_3 extends NonDeterminism {

		/**
		 * @param name
		 * @throws AbCAttributeTypeException
		 */
		public Process_3() throws AbCAttributeTypeException {
			// TODO Auto-generated constructor stub
			super();
		}

		/* (non-Javadoc)
		 * @see org.sysma.abc.core.AbCProcess#doRun()
		 */
		@Override
		protected void doRun() throws Exception {
			// TODO Auto-generated method stub
			GroupPredicate p1 = new Or(new HasValue("$1", this.getComponent().getStore().getValue("subscription1")),
					new HasValue("$2", this.getComponent().getStore().getValue("subscription1")));
			GroupPredicate p2 = new HasValue("$1", this.getComponent().getStore().getValue("subscription3"));
			HashMap<GroupPredicate, AbCProcess> map=new HashMap<>();
			map.put(p1, new Process_1());
			map.put(p2, new Process_2());
			map.put(p2, new Process_2());
			SetProcess(map);
			resolve();
		}

	}

	public static void main(String[] args) throws AbCAttributeTypeException, IOException, DuplicateNameException {
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
		// Process_1 subscriber1 = new Process_1("subscriber_1");
		// Process_2 subscriber2 = new Process_2("subscriber_2");
		Process_3 main = new Process_3();
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("subscription1", Object.class);
		Attribute<Object> a2 = new Attribute<Object>("subscription2", Object.class);
		Attribute<Object> a3 = new Attribute<Object>("subscription3", Object.class);
		store1.setValue(a1, "Movies");
		store1.setValue(a2, "News");
		store1.setValue(a3, "Songs");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(main);
		c1.addPort(cPortClient);
		cPortClient.start();
		c1.start();
		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
