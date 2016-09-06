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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.distributed.MessageReceiver;
import org.sysma.abc.core.topology.distributed.PortHandler;

/**
 * @author Yehia Abd Alrahman, Michele Loreti
 *
 */
public class AbCComponent implements MessageReceiver {

	/**
	 * Component store
	 */
	protected AbCEnvironment store;

	/**
	 * Component name
	 */
	protected String nameString;

	/**
	 * List of processes currently running at the component.
	 */
	protected LinkedList<AbCProcess> processes;
	
	/**
	 * List of processes waiting for the execution at the component.
	 */
	protected LinkedList<AbCProcess> waiting;

	/**
	 * Process counter used to compute process ids
	 */
	protected int processCounter = 0;
	
	/**
	 * Component state
	 */
	private ComponentState state;
	
	/**
	 * Component 
	 */
	private int nameCounter = 0;

	/**
	 * Tread Executor of this component
	 */
	protected Executor executor = Executors.newCachedThreadPool();

	/**
	 * Port used to access to the underlying network topology
	 */
	protected AbCPort port;
	
	
	public int executed = 0;

	/**
	 * @return the nameCounter
	 */
	public int getNameCounter() {
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


	/**
	 * @param executed
	 *            the executed to set
	 */
	public synchronized void setExecuted(int executed, Boolean rec) {
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

	public AbCComponent(String name) {
		this( name , new AbCEnvironment() );
	}
	
	public AbCComponent(String name, AbCEnvironment store) {
		this.nameString = name;
		this.state = ComponentState.READY;
		this.store = store;
		this.processes = new LinkedList<AbCProcess>();
		this.waiting = new LinkedList<AbCProcess>();
		this.port = null;
	}

	public String getName() {
		return this.nameString;
	}

	public PortHandler connect() {
		if (port == null) {
			throw new IllegalStateException();
		} else {
			return port.connect();
		}
	}
	
	public synchronized void setPort(AbCPort p) throws DuplicateNameException {

		this.port = p;
		this.port.setReceiver(this);

	}

	public <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		return store.getValue(attribute);
	}

	public <T> void setValue(Attribute<T> attribute, T value) throws AbCAttributeTypeException {
		store.setValue(attribute, value);
	}

	public synchronized void addProcess(AbCProcess process) {

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
	public synchronized void done(AbCProcess abCProcess) {
		processes.removeFirstOccurrence(abCProcess);
		notifyAll();
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
		process.setComponent(process.getProcessId(), this);
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
		try {
			if (message.getPredicate().isSatisfiedBy(store)) {
				Object value = message.getValue();
				LinkedList<AbCProcess> copyList = new LinkedList<>( processes );
				for (AbCProcess p : copyList) {
					AbCEnvironment update = p.deliver(value);
					if ( update != null ) {
						try {
							store.update( update );
						} catch (AbCAttributeTypeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return ;
					}
				}		
			}
		} catch (AbCAttributeTypeException e) {
			e.printStackTrace();
		}
	}

	public Attribute<?> getAttribute(String name) {
		return store.getAttribute(name);
	}

	public void waitUntil(AbCPredicate p) throws AbCAttributeTypeException, InterruptedException {
		store.waitUntil(p);
	}

}
