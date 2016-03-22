/**
 * 
 */
package org.sysma.abc.core.Examples.GroupBased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.AbcUpdate;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.centralized.ServerPortAddress;
import org.sysma.abc.core.centralized.ServerPortClient;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;
import org.sysma.abc.core.grpPredicate.NoComponent;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class GroupC {
	public static GroupPredicate FromgrpA = new HasValue("$1", "A");
	public static GroupPredicate ff=new NoComponent();
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
			System.out.println(this.name + " => received: " + receive(FromgrpA, new AbcUpdate() {

				@Override
				public Map<Attribute<?>, Object> eval(Object o) {
					HashMap<Attribute<?>, Object> update = new HashMap<>();
					update.put(getComponent().getStore().getAttribute("group"), o);
					return update;
				}})
			
				);
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
			update.put(this.getComponent().getStore().getAttribute("group"), "B");
			Thread.sleep(20000);
			Send(ff, " ", update);
			System.out.println("joined group B");
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
		Process_1 rcv = new Process_1("rcv_1");
		Process_2 join=new Process_2("joinA");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("group", Object.class);
		store1.setValue(a1, "C");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(rcv);
		c1.addProcess(join);
		c1.addPort(cPortClient);
		cPortClient.start();
		c1.start();
		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
