/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.AnyComponent;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.HasValue;
import org.sysma.abc.core.grpPredicate.NoComponent;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class TryBroadcast {

	public static VirtualPort vp = new VirtualPort(10);

	public static GroupPredicate no = new NoComponent();
	public static GroupPredicate any = new AnyComponent();

	public static class inBroadcast extends InputBroadcastAction {

		/**
		 * @param predicate
		 * @param value
		 * @param update
		 */
		public inBroadcast(GroupPredicate predicate, HashMap<Attribute<Object>, Object> update) {
			super(predicate, update);
			// TODO Auto-generated constructor stub
			this.update = update;
		}

	}

	public static class OutBroadcast extends BroadcastAction {

		/**
		 * @param predicate
		 * @param s
		 * @param value
		 * @param update
		 */
		public OutBroadcast(GroupPredicate predicate, Set<Attribute<Object>> s, Object value,
				HashMap<Attribute<Object>, Object> update) {
			super(predicate, s, value, update);
			// TODO Auto-generated constructor stub
			this.update = update;
		}

	} 

	public static class Broadcaster extends AbCProcess {

		/**
		 * @param component
		 * @param name
		 */
		public Broadcaster(AbCComponent component, String name, AbCAction... abCActions) {
			super(component, name);
			// TODO Auto-generated constructor stub
			for (AbCAction abCAction : abCActions) {
				insertAction(abCAction);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.sysma.abc.core.AbCProcess#doRun()
		 */
		@Override
		protected void doRun() {
			// TODO Auto-generated method stub

			try {
				Broadcast(any, getActions().peek().getExposed(), "test_1", null);
			} catch (AbCAttributeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public static class Receiver extends AbCProcess {

			/**
			 * @param component
			 * @param name
			 */
			public Receiver(AbCComponent component, String name, AbCAction... abCActions) {
				super(component, name);
				// TODO Auto-generated constructor stub

				for (AbCAction abCAction : abCActions) {
					insertAction(abCAction);
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.sysma.abc.core.AbCProcess#doRun()
			 */
			@Override
			protected void doRun() throws InterruptedException, AbCAttributeTypeException {
				// TODO Auto-generated method stub

				System.out.println(
						this.name + " => received: " + receive(getActions().peek().getPredicate(), null, null));

			}
		}

		public static void main(String[] args)
				throws AbCAttributeTypeException, DuplicateNameException, InterruptedException {
			// TODO Auto-generated method stub

			Set<Attribute<Object>> set = new HashSet<>();
			Attribute<Object> a1 = new Attribute<Object>("role", Object.class);
			set.add(a1);
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
			Broadcaster brd = new Broadcaster(c1, "brd", new OutBroadcast(any, set, "test_1", null));
			Receiver rcv1 = new Receiver(c2, "rcv1_c2", new inBroadcast(any, null));
			Receiver rcv2 = new Receiver(c3, "rcv2_c3", new inBroadcast(any, null));
			Receiver rcv3 = new Receiver(c2, "rcv3_c2", new inBroadcast(any, null));
			Receiver rcv4 = new Receiver(c1, "rcv4_c1", new inBroadcast(any, null));
			c1.addProcess(brd);
			c1.addProcess(rcv4);
			c2.addProcess(rcv3);
			c2.addProcess(rcv1);
			c3.addProcess(rcv2);
			c1.addPort(vp);
			c2.addPort(vp);
			c3.addPort(vp);
			c1.start();
			c2.start();
			c3.start();
			System.out.println(c1.getStore().toString());
			Thread.sleep(3000);
		}

	}
}
