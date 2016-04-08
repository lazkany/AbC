/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.NoComponent;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AbCComponent {

	protected AbCEnvironment store;

	protected String nameString;

	protected LinkedList<AbCProcess> processes;
	protected LinkedList<AbCProcess> waiting;

	protected int processCounter = 0;
	private ComponentState state;
	private int nameCounter = 0;

	protected Executor executor = Executors.newCachedThreadPool();

	protected LinkedList<AbCPort> ports;
	public int executed = 0;

	private Boolean rec = false;

	/**
	 * @return the nameCounter
	 */
	public synchronized int getNameCounter() {
		nameCounter++;
		return nameCounter;
	}

	/**
	 * @param nameCounter
	 *            the nameCounter to set
	 */
	public void setNameCounter(int nameCounter) {
		this.nameCounter = nameCounter;
	}

	// /**
	// * @return the executed
	// * @throws InterruptedException
	// */
	// public int getExecuted() throws InterruptedException {
	// // while(executed.isEmpty())
	// // {
	// // wait();
	// // }
	// return executed;
	// }

	/**
	 * @param executed
	 *            the executed to set
	 */
	public synchronized void setExecuted(int executed, Boolean rec) {
		this.rec = rec;
		this.executed = executed;
		notifyAll();
	}

	/**
	 * @return the executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @return the processes
	 */
	public LinkedList<AbCProcess> getProcesses() {
		return processes;
	}

	public AbCComponent(String name, AbCEnvironment store) {
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

	public synchronized void send(GroupPredicate predicate, Object value, HashMap<Attribute<?>, Object> update)
			throws AbCAttributeTypeException {

		AbCMessage message = new AbCMessage(this, value, predicate);
		if (!predicate.equals(new NoComponent())) {
			for (AbCPort port : ports) {
				port.send(message);
			}
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

		process.id = getNameCounter();
		if (isRunning()) {
			_addProcess(process);

		} else {
			waiting.add(process);
		}

	}

	public AbCEnvironment getStore() {
		return store;
	}

	public AbCEnvironment exposedStore(Set<Attribute<?>> exposed) throws AbCAttributeTypeException {
		AbCEnvironment temp = new AbCEnvironment();
		for (Attribute<?> att : exposed) {

			temp.setValue(att, store.getValue(att));
		}
		return temp;

	}

	protected void setStore(AbCEnvironment store) {
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
		return nameString + ":" + store.toString() + ":" + processes.toString();
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

	public <T> void storeUpdate(Map<Attribute<?>, Object> update) throws AbCAttributeTypeException {
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

	public synchronized void receive(AbCMessage message) throws InterruptedException {
		loop: for (AbCProcess p : processes) {
			if (p.getProcessType() == "receiver") {
				p.receive(message);
				while (executed != 1) {
					wait();
				}
				if (rec) {
					setExecuted(0, false);
					break loop;
				}
			}
			setExecuted(0, false);
		}

	}

}
