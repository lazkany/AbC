/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.grpPredicate.GroupPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCProcess implements Runnable {

	private AbCComponent component;
	protected Queue<AbCAction> actions = new LinkedList<AbCAction>();
	protected String name;
	protected int id;

	
	/**
	 * @return the actions
	 */
	protected Queue<AbCAction> getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	protected void insertAction(AbCAction action) {
		this.actions.add(action);
	}

	public AbCProcess(String name) {
		this.name = name;
	}

	public AbCProcess(AbCComponent component, String name) {
		this.name = name;
		this.component = component;
	}

	public void addAction(AbCAction action) {
		actions.add(action);
	}

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

	protected AbCStore getStore() {
		return component.store;
	}

	// public <T> void storeUpdate(HashMap<Attribute<T>, T> update) throws
	// AbCAttributeTypeException {
	// for (Attribute<T> att : update.keySet()) {
	//
	// component.store.setValue(att, update.get(att));
	// }
	// }

	public <T> AbCStore ExposedStore(Set<Attribute<T>> exposed) throws AbCAttributeTypeException {
		AbCStore temp = new AbCStore();
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
	 * @param value
	 * @return
	 * @throws InterruptedException
	 * @throws AbCAttributeTypeException 
	 */
	protected Object receive(GroupPredicate predicate, Object value, HashMap<Attribute<Object>, Object> update)
			throws InterruptedException, AbCAttributeTypeException {
		// TODO Auto-generated method stub
		// TODO GET STORE
		AbCStore store = this.component.getStore();

		// TODO recieve the broadcast
		return component.Broadcastinput(this, predicate, store, value, update);

		// TODO update the store
	}

	/**
	 * @param predicate
	 * @param s
	 * @param value
	 * @return
	 * @throws AbCAttributeTypeException
	 */
	protected Object Broadcast(GroupPredicate predicate, Set<Attribute<Object>> s, Object value,
			HashMap<Attribute<Object>, Object> update) throws AbCAttributeTypeException {
		// TODO Auto-generated method stub
		// TODO Compute the exposed store
		AbCStore store = this.ExposedStore(s);
		// AbCStore store = new AbCStore();
		// TODO update the store
		return component.Broadcastoutput(this, predicate, store, value, update);
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
//			for (AbCProcess process : component.getProcesses()) {
//				
//			}
		}

	}

	/**
	 * @param e
	 */
	private void doHandle(Exception e) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	protected void doClose() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	protected abstract void doRun() throws Exception;

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

	// public void exec(AbCProcess p) throws InterruptedException {
	// if (p != null) {
	// component.exec(this, p);
	// }
	// }

}
