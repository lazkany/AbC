/**
 * 
 */
package org.sysma.abc.core.ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Or;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.VirtualPort;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Virtualexample {
	public static VirtualPort vp = new VirtualPort();

	public static AbCPredicate no = new FalsePredicate();
	public static AbCPredicate any = new TruePredicate();
	/**
	 * 
	 */
	public Virtualexample() {
		// TODO Auto-generated constructor stub
	}
	public static class Process_1 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_1(String name) {
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
			AbCPredicate subscribe = new Or(new HasValue("$1", this.getComponent().getStore().getValue("subscription")), new HasValue("$2", this.getComponent().getStore().getValue("subscription")));
			System.out.println(this.name + " => received: " + receive(subscribe));


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
			AbCPredicate subscribe = new Or(new HasValue("$1", this.getComponent().getStore().getValue("subscription")), new HasValue("$2", this.getComponent().getStore().getValue("subscription")));
			System.out.println(this.name + " => received: " + receive(subscribe));


		}
	}
	public static class Process_3 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_3(String name) {
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
			send(any, "msg," + this.getComponent().getStore().getValue("topic_1") + "," + this.getComponent().getStore().getValue("topic_2"));
		}
	}
	public static class Process_4 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_4(String name) {
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
			while(true)
			{
				
			}

		}
	}
	public static void main(String[] args) throws IOException, DuplicateNameException, AbCAttributeTypeException {
		// TODO Auto-generated method stub
		System.out.println("Start");
		String text;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			text = (bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		Process_1 subscriber1 = new Process_1("subscriber_1");
		AbCEnvironment store1 = new AbCEnvironment();
		Attribute<Object> a1 = new Attribute<Object>("subscription", Object.class);
		store1.setValue(a1, "Songs");
		AbCComponent c1 = new AbCComponent("C1", store1);
		c1.addProcess(subscriber1);
		c1.setPort(vp.getPort());
		c1.start();
		

	}
}
