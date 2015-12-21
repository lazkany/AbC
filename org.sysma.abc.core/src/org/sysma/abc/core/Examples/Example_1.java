/**
 * 
 */
package org.sysma.abc.core.Examples;

import java.util.HashSet;
import java.util.Set;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.centralized.VirtualPort;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.AnyComponent;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;
import org.sysma.abc.core.grpPredicate.NoComponent;

import com.google.gson.Gson;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Example_1 {
	public static VirtualPort vp = new VirtualPort(10);

	public static GroupPredicate no = new NoComponent();
	public static GroupPredicate any = new AnyComponent();

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
		protected void doRun() throws InterruptedException {
			// TODO Auto-generated method stub
			Set<Attribute<Object>> expose = new HashSet<>();
			Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
			expose.add(a1);
			try {
				Broadcast(any, expose, "test_send", null);
				System.out.println(this.name + " => received: " +receive(any, null));
				//System.out.println(this.name + " => received: " +receive(any, null));
			//	Broadcast(any, expose, "test_2", null);
			} catch (AbCAttributeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class Process_2 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_2(String name) {
			super(null, name);
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

//			 System.out.println(this.name + " => received: " +receive(any, null));
//		
//			 System.out.println(this.name + " => received: " +receive(any, null));
			Broadcast(any, expose, "test_1", null);
			Broadcast(any, expose, "test_2", null);

		}
	}

	/**
	 * 
	 */
	public Example_1() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws AbCAttributeTypeException 
	 * @throws DuplicateNameException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws AbCAttributeTypeException, DuplicateNameException, InterruptedException {
		// TODO Auto-generated method stub
		Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
		Attribute<Object> a2 = new Attribute<Object>("status", Object.class);
		Attribute<Object> a3 = new Attribute<Object>("location", Object.class);
		AbCStore store1 = new AbCStore();
		store1.setValue(a1, "explorer");
		store1.setValue(a2, "on");
		store1.setValue(a3, "<2,5>");
		AbCStore store2 = new AbCStore();
		store2.setValue(a1, "rescuer");
		store2.setValue(a2, "off");
		store2.setValue(a3, "<3,4>");
		AbCStore store3 = new AbCStore();
		store3.setValue(a1, "charcher");
		store3.setValue(a2, "off");
		store3.setValue(a3, "<1,6>");
		AbCComponent c1 = new AbCComponent("C1", store1);
		AbCComponent c2 = new AbCComponent("C2", store2);
		AbCComponent c3 = new AbCComponent("C3", store3);
		GroupPredicate checkpro = new HasValue("role", "rescuer");
		GroupPredicate self = new HasValue("role", store2.getValue(a1));
		Process_1 brd1 = new Process_1("brd_1");
		Process_1 brd2 = new Process_1("brd_2");
		Process_2 rcv1 = new Process_2("rcv1_c2");
		Process_2 rcv2 = new Process_2("rcv2_c3");
		Process_2 rcv3 = new Process_2("rcv3_c2");
		Process_2 rcv4 = new Process_2("rcv4_c1");
		c1.addProcess(brd1);
		c1.addProcess(rcv4);
//		c2.addProcess(rcv3); 
		c2.addProcess(rcv1);
		//c3.addProcess(rcv2);
//		c1.addPort(vp);
//		c2.addPort(vp);
//	//	c3.addPort(vp);
//		vp.start();
//		c1.start();
//		c2.start();
	//	c3.start();
		Gson gson=AbCFactory.getGSon();
		
	System.out.println(gson.toJson(a1));
		Thread.sleep(3000);

	}

}
