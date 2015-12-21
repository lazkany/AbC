/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AbCComponent {

	protected AbCStore store;

	protected String nameString;
								
	protected LinkedList<AbCProcess> processes;
	protected LinkedList<AbCProcess> waiting;
	
	protected int processCounter = 0;
	private ComponentState state;
	protected int nameCounter = 0;
	protected Executor executor = Executors.newCachedThreadPool();
	protected LinkedList<AbCPort> ports;

	// /**
	// * @return the upcomming
	// * @throws InterruptedException
	// */
	// public synchronized HashMap<Object, AbCStore> getUpcomming() throws
	// InterruptedException {
	// while (upcomming.isEmpty()) {
	// wait();
	// }
	// return upcomming.peek();
	// }
	//
	// /**
	// * @param upcomming
	// * the upcomming to set
	// */
	// public synchronized void setUpcomming(HashMap<Object, AbCStore>
	// upcommingMsg) {
	// this.upcomming.clear();
	// ;
	// this.upcomming.add(upcommingMsg);
	// notifyAll();
	// }

	/**
	 * @return the processes
	 */
	public LinkedList<AbCProcess> getProcesses() {
		return processes;
	}

	public AbCComponent(String name, AbCStore store) {
		this.nameString = name;
		this.state = ComponentState.READY;
		this.store = store;
		this.processes = new LinkedList<AbCProcess>();
		this.waiting = new LinkedList<AbCProcess>();
		this.ports = new LinkedList<AbCPort>();
	}

	public String getName() {
		return this.nameString;
	}

	public synchronized void send(GroupPredicate predicate, AbCStore store, Object value,
			HashMap<Attribute<?>, Object> update) throws AbCAttributeTypeException {

		AbCMessage message = new AbCMessage(this, value, predicate, store);

		for (AbCPort port : ports) {
			port.send(message);
		}

		this.storeUpdate(update);

	}

	// public Object Broadcastinput(AbCProcess process, GroupPredicate
	// predicate, AbCStore store,
	// Object value, HashMap<Attribute<Object>, Object> update)
	// throws InterruptedException, AbCAttributeTypeException {
	// HashMap<Object, AbCStore> temp = getUpcomming();
	// AbCStore upcomingStore = (AbCStore) temp.values().toArray()[0];
	// if (!predicate.isSatisfiedBy(upcomingStore)) {
	// Broadcastinput(process, predicate, store, value, update);
	// }
	//
	// return temp.keySet().toArray()[0];
	// }

	public synchronized void addPort(AbCPort p) throws DuplicateNameException {
		p.register(this);
		ports.add(p);
	}

	public <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		return store.getValue(attribute);
	}

	public <T> void setValue(Attribute<T> attribute, T value) throws AbCAttributeTypeException {
		store.setValue(attribute, value);
	}

	public void addProcess(AbCProcess process) {
		if (isRunning()) {
			_addProcess(process);

		} else {
			waiting.add(process);
		}

	}

	public AbCStore getStore() {
		return store;
	}

	public AbCStore exposedStore(Set<Attribute<?>> exposed) throws AbCAttributeTypeException {
		AbCStore temp = new AbCStore();
		for (Attribute<?> att : exposed) {

			temp.setValue(att, store.getValue(att));
		}
		return temp;

	}

	protected void setStore(AbCStore store) {
		this.store = store;
	}

	public boolean isRunning() {

		return state == ComponentState.RUNNING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return store.toString() + ":" + processes.toString();
	}

	/**
	 * @param abCProcess
	 */
	public void done(AbCProcess abCProcess) {
		// TODO Auto-generated method stub
		// abCProcess.setProState(State.HALT);
	}

	/**
	 * @param abCProcess
	 * @return
	 */
	public synchronized String fresh() {
		// TODO Auto-generated method stub
		return nameString + ":" + (nameCounter++);
	}

	/**
	 * @param time
	 * @throws InterruptedException
	 */
	public void suspend(long time) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread.sleep(time);
	}

	// /**
	// * @param a
	// * @param b
	// */
	// public void exec(AbCProcess a, AbCProcess b) {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * Terminates node
	 */
	private void doStop() {
		// TODO Auto-generated method stub

	}

	protected synchronized int getProcessID() {
		return processCounter++;
	}

	protected synchronized ComponentState getState() {
		return state;
	}

	public <T> void storeUpdate(HashMap<Attribute<?>, Object> update) throws AbCAttributeTypeException {
		if (update != null) {
			for (Attribute<?> att : update.keySet()) {
				store.setValue(att, update.get(att));
			}
		}

	}

	public synchronized void start() {

		for (AbCProcess p : waiting) {
			_addProcess(p);
		}

		state = ComponentState.RUNNING;
	}

	/**
	 * @param p
	 */
	private void _addProcess(AbCProcess process) {
		// TODO Auto-generated method stub
		process.setComponent(process.GetProcessId(), this);
		processes.add(process);
		executor.execute(process);
	}

	public synchronized void stop() {
		if (state != ComponentState.RUNNING) {
			throw new IllegalStateException();
		}
		state = ComponentState.HALT;
		notifyAll();
		doStop();
	}

	protected synchronized void waitState(ComponentState state) throws InterruptedException {
		while (getState() != state) {
			wait();
		}
	}

	public synchronized void receive(AbCMessage message) {
		for (AbCProcess p : processes) {
			if (p.getProcessType()=="receiver") {			//CHANGE> Messages are only sent to receiving processes
				p.receive(message);
			}
			
		}

	}

}
