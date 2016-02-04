/**
 * 
 */
package org.sysma.abc.core.Examples.GroupBased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.Examples.Example_1.Process_1;
import org.sysma.abc.core.Examples.Example_1.Process_2;
import org.sysma.abc.core.centralized.ServerPortAddress;
import org.sysma.abc.core.centralized.ServerPortClient;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.AnyComponent;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Sender {
	public static GroupPredicate any = new AnyComponent();

	//
	public static class Process_1 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_1(String name) {
			super(name);
			// TODO Auto-generated constructor stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.sysma.abc.core.AbCProcess#doRun()
		 */
		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {
			// TODO Auto-generated method stub
			Set<Attribute<Object>> expose = new HashSet<>();
			Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
			expose.add(a1);
			//while (true) {
				Send(any, "test_send", null);
				//System.out.println(this.name + " => received: " + receive(any, null));
			//}
				
			
			// System.out.println(this.name + " => received: " +receive(any,
			// null));
			// Broadcast(any, expose, "test_2", null);
		}
	}


	/**
	 * @param args
	 * @throws IOException 
	 * @throws AbCAttributeTypeException 
	 * @throws DuplicateNameException 
	 */
	public static void main(String[] args) throws IOException, AbCAttributeTypeException, DuplicateNameException {
		// TODO Auto-generated method stub
		System.out.println("Enter port number : ");
		   int port = 0;
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    port = Integer.parseInt(bufferRead.readLine());
		    
		    
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		ServerPortClient cPortClient = new ServerPortClient(new ServerPortAddress(9998), new ServerSocket(port));
		cPortClient.RemoteRegister(new ServerPortAddress(9999));
		Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
		Attribute<Object> a2 = new Attribute<Object>("status", Object.class);
		Attribute<Object> a3 = new Attribute<Object>("location", Object.class);
		AbCEnvironment store1 = new AbCEnvironment();
		store1.setValue(a1, "explorer");
		store1.setValue(a2, "on");
		store1.setValue(a3, "<2,5>");
		AbCEnvironment store2 = new AbCEnvironment();
		store2.setValue(a1, "rescuer");
		store2.setValue(a2, "off");
		store2.setValue(a3, "<3,4>");
		AbCEnvironment store3 = new AbCEnvironment();
		store3.setValue(a1, "charcher");
		store3.setValue(a2, "off");
		store3.setValue(a3, "<1,6>");
		AbCComponent c1 = new AbCComponent("C1", store1);
		AbCComponent c2 = new AbCComponent("C2", store2);
		AbCComponent c3 = new AbCComponent("C3", store3);
		GroupPredicate checkpro = new HasValue("role", "rescuer");
		GroupPredicate self = new HasValue("role", store2.getValue(a1));
		Process_1 brd1 = new Process_1("brd_1");
//		Process_1 brd2 = new Process_1("brd_2");
//		Process_2 rcv1 = new Process_2("rcv1_c2");
//		Process_2 rcv2 = new Process_2("rcv2_c3");
//		Process_2 rcv3 = new Process_2("rcv3_c2");
//		Process_2 rcv4 = new Process_2("rcv4_c1");
		c1.addProcess(brd1);
//		c1.addProcess(rcv4);
//		 c2.addProcess(rcv3);
//		c2.addProcess(rcv1);
//		 c3.addProcess(rcv2);
		 c1.addPort(cPortClient);
		// c2.addPort(cPortClient);
		// // c3.addPort(cPortClient);
		// vp.start();
		// 
		// c2.start();
		// c3.start();
		cPortClient.start();
		c1.start();
		System.out.println(cPortClient.getAddress());

	}

}
