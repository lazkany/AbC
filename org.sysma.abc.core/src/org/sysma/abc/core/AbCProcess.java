/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import javax.sql.rowset.spi.SyncResolver;
import javax.swing.Box.Filler;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.NoComponent;
import java.util.function.*;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCProcess implements Runnable {

	private AbCComponent component;
	// protected Queue<AbCAction> actions = new LinkedList<AbCAction>();
	protected String name;
	protected int id;
	public Queue<AbCMessage> receivedMessage = new LinkedList<>();
	private String processType = "";
	private boolean interrupt = false;

	// /**
	// * @return the actions
	// */
	// protected Queue<AbCAction> getActions() {
	// return actions;
	// }
	//
	// /**
	// * @param actions
	// * the actions to set
	// */
	// protected void insertAction(AbCAction action) {
	// this.actions.add(action);
	// }

	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}

	public AbCProcess(String name) {
		this.name = name;

	}

	public AbCProcess() {

	}

	public AbCProcess(AbCComponent component, String name) {
		this.name = name;
		this.component = component;
	}

	// public void addAction(AbCAction action) {
	// actions.add(action);
	// }
	//
	public int GetProcessId() {
		return this.id;
	}

	protected void setComponent(int id, AbCComponent component) {
		this.id = id;
		this.component = component;
	}

	protected synchronized AbCComponent getComponent() {
		return this.component;
	}

	protected AbCEnvironment getStore() {
		return component.store;
	}

	// public <T> void storeUpdate(HashMap<Attribute<T>, T> update) throws
	// AbCAttributeTypeException {
	// for (Attribute<T> att : update.keySet()) {
	//
	// component.store.setValue(att, update.get(att));
	// }
	// }

	public <T> AbCEnvironment ExposedStore(Set<Attribute<T>> exposed) throws AbCAttributeTypeException {
		AbCEnvironment temp = new AbCEnvironment();
		for (Attribute<T> att : exposed) {

			temp.setValue(att, component.store.getValue(att));
		}
		return temp;

	}

	public <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		return component.store.getValue(attribute);
	}

	public <T> void setValue(Attribute<T> attribute, T value) throws AbCAttributeTypeException {
		component.store.setValue(attribute, value);
	}

	public String getName() {
		return name;
	}

	/**
	 * @param predicate
	 * @param s
	 * @param value
	 * @return
	 * @throws AbCAttributeTypeException
	 */
	protected void Send(GroupPredicate predicate, Object value, HashMap<Attribute<?>, Object> update)
			throws AbCAttributeTypeException {
		// TODO Auto-generated method stub
		// TODO Compute the exposed store
		this.processType = "sender"; // CHANGE> A process has to declare its
										// type
		// AbCStore store = new AbCStore();
		// AbCStore store = new AbCStore();
		// TODO update the store
		// if (predicate.isSatisfiedBy(this.component.getStore())) { // CHANGE>
		// // Send the
		// // message
		// // also to
		// // co-located
		// // processes
		// this.component.receive(new AbCMessage(this.component, value,
		// predicate));
		// this.receivedMessage.poll(); // Ensure that the sender queue is
		// // empty.
		// }

		component.send(predicate, value, update);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			doStart();
			doRun();
			doClose();
		} catch (Exception e) {
			doHandle(e);
		} finally {
			component.done(this);
			// for (AbCProcess process : component.getProcesses()) {
			//
			// }
		}

	}

	/**
	 * @param e
	 */
	private void doHandle(Exception e) {
		// TODO Auto-generated method stub
		this.component.getProcesses().remove(this);
	}

	/**
	 * 
	 */
	protected void doClose() {
		// TODO Auto-generated method stub
		// this.component.getProcesses().remove(this);
	}

	/**
	 * 
	 */
	protected abstract void doRun() throws Exception;

	/*
	 * P = { p_1 }( x ).( x+1 )@{ p_2 }.P
	 * 
	 * class P extends AbCProcess {
	 * 
	 * protected void doRun() { exexuteP(); }
	 * 
	 * private executeP() { double x = receive( [| p_1 |] ); broadcast( x+1 , [|
	 * p_2 |] ); executeP(); }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 */

	/**
	 * 
	 */
	protected void doStart() {
		// TODO Auto-generated method stub

	}

	public void call() {
		try {
			doRun();
		} catch (Exception e) {
			doHandle(e);
		}
	}

	public String fresh() throws InterruptedException {
		return component.fresh();
	}

	public void call(AbCProcess process) {
		process.component = this.component;
		process.call();
	}

	public void suspend(long time) throws InterruptedException {
		component.suspend(time);
	}

	public void interrupt() throws InterruptedException {
		// this.component.processes.remove(this);
		interrupt = true;
		notifyAll();
	}

	public void interrupt(String name) throws InterruptedException {
		for (AbCProcess abCProcess : this.component.processes) {
			if (name == abCProcess.getName()) {

				abCProcess.interrupt();
			}
		}
	}

	public void interrupt(int id) throws InterruptedException {
		for (AbCProcess abCProcess : this.component.processes) {
			if (id == abCProcess.id) {

				abCProcess.interrupt();
			}
		}
		// this.component.processes.removeIf(P -> P.GetProcessId() == id);

		// Thread.currentThread().interrupt();

		// return;
	}

	protected synchronized Object receive(GroupPredicate predicate, AbcUpdate update)
			throws InterruptedException, AbCAttributeTypeException {

		this.processType = "receiver";
		System.out.println(GetProcessId());
		while (true) {

			while (receivedMessage.isEmpty() && !interrupt) {
				wait();
			}
			if (interrupt) {
				this.processType = "";
				// component.setNrcv(-1);
				Thread.currentThread().interrupt();
			}
			Object value = receivedMessage.peek().getValue(predicate);
			if (value != null) { // CHANGE>

				// this.component.storeUpdate(update.eval(value));
				this.component.setExecuted(1, true);
				this.processType = "";
				System.out.println("t" + GetProcessId() + "exec" + component.executed);
				return receivedMessage.poll().getValue(predicate);
			}

			receivedMessage.poll();
			this.component.setExecuted(1, false);
		}

	}

	protected synchronized void receive(AbCMessage msg) {
		// if (waitingMessage) {
		receivedMessage.add(msg);
		notifyAll();
		// }   
	}

	public void exec(AbCProcess p) throws InterruptedException {
		// if (p != null) {

		p.id = this.component.getNameCounter();

		p.setComponent(p.GetProcessId(), this.component);
		this.component.getProcesses().add(p);
		this.component.getExecutor().execute(p);
		// new Thread(p).start();
		// component.exec(this, p);
		// }
	}

}
