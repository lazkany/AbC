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

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.topology.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class GroupA {
	public static AbCPredicate TOgrpB = new HasValue("group", "B");

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
			// TODO Auto-generated method stub
			while(true){
			send(TOgrpB, new Tuple("MSG",this.getComponent().getStore().getValue("group")) );
			Thread.sleep(2000);
			}
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
		Process_1 snd = new Process_1("send_1");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("group", Object.class);
		store1.setValue(a1, "A");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(snd);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress());

	}

}
