/**
 * 
 */
package org.sysma.abc.core;

import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.topology.PortHandler;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCProcess implements Runnable {

	
	/**
	 * Hosting component
	 */
	private AbCComponent component;

	/**
	 * Process name
	 */
	protected String name;
	
	/**
	 * Process id
	 */
	protected int id;
	
	protected boolean isDelivering = false;
	
	private boolean interrupt = false;

	private Object received;
	
	private InputAction[] waitingList;
	
	private int selectedBranch = -1;

	private boolean readyToReceive = false;

	public AbCProcess(String name) {
		this(null,name);
	}

	public AbCProcess() {
		this(null,"<UNNAMED>");
	}

	public AbCProcess(AbCComponent component, String name) {
		this.name = name;
		this.component = component;
	}

	public int getProcessId() {
		return this.id;
	}

	protected void setComponent(int id, AbCComponent component) {
		this.id = id;
		this.component = component;
	}

	protected AbCComponent getComponent() {
		return this.component;
	}
  
	public <T> AbCEnvironment ExposedStore(Set<Attribute<T>> exposed) throws AbCAttributeTypeException {
		AbCEnvironment temp = new AbCEnvironment();
		for (Attribute<T> att : exposed) {

			temp.setValue(att, component.store.getValue(att));
		}
		return temp;

	}

	public <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		return component.getValue(attribute);
	}
	
	public Attribute<?> getAttribute( String name ) {
		return component.getAttribute( name );
	}

	public <T> void setValue(Attribute<T> attribute, T value) throws AbCAttributeTypeException {
		component.setValue(attribute, value);
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
	 * @throws InterruptedException 
	 */
	protected void send(AbCPredicate predicate, Object value)
			throws AbCAttributeTypeException, InterruptedException {

		synchronized (this) {
			readyToReceive = true;
			notifyAll();
			while (isDelivering) {
				wait();
			}			
		}
		PortHandler handler = component.connect();
		synchronized(this) {
			handler.send(new AbCMessage(value, predicate));
			readyToReceive = false;
			notifyAll();
		}
	}

	/**
	 * @param predicate
	 * @param s
	 * @param value
	 * @return
	 * @throws AbCAttributeTypeException
	 * @throws InterruptedException 
	 */
	protected synchronized void asend(AbCPredicate predicate, Object value)
			throws AbCAttributeTypeException, InterruptedException {

		exec( new AbCProcess() {
			
			@Override
			protected void doRun() throws Exception {
				send( predicate,value);
			}
			
		});
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
		}

	}

	/**
	 * @param e
	 */
	protected void doHandle(Exception e) {
		e.printStackTrace();
	}

	/**
	 * 
	 */
	protected void doClose() {

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

	public synchronized void interrupt() {
		interrupt = true;
		notifyAll();
	}
	
	protected Object receive( Function<Object,AbCPredicate> predicate ) throws InterruptedException {
		return receive( new InputAction( predicate ) );
	}
	
	protected Object receive( AbCPredicate predicate ) throws InterruptedException {
		return receive( new InputAction( e -> predicate ) );
	}

	protected synchronized Object receive( InputAction ... actions ) throws InterruptedException {		
		readyToReceive = true;
		notifyAll();
		waitingList = actions;
		selectedBranch = -1;
		received = null;
		while (received == null) {
			wait();
		}
		return received;
	}

	protected synchronized AbCEnvironment deliver(Object value) {
		AbCEnvironment toReturn = null;
		this.isDelivering = true;
		notifyAll();
		while ( !readyToReceive ) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.isDelivering = false;
		if (waitingList != null) {
			int i = 0;
			AbCEnvironment update = null;
			while ((update == null)&&(i<waitingList.length)) {
				update = waitingList[i].deliverMessage(component.getStore(), value); 
				i++;
			}
			if (update != null) {
				received = value;
				selectedBranch = i;
				waitingList = null;
				readyToReceive = false;
				toReturn = update;
			} 			
		} 
		isDelivering = false;
		notifyAll();
		return toReturn;
	}

	public void exec(AbCProcess p) throws InterruptedException {
		// if (p != null) {

		p.id = this.component.getNameCounter();

		p.setComponent(p.getProcessId(), this.component);
		this.component.getProcesses().add(p);
		this.component.getExecutor().execute(p);
		// new Thread(p).start();
		// component.exec(this, p);
		// }
	}
		
	public void waitUnil( AbCPredicate p ) throws AbCAttributeTypeException, InterruptedException {
		synchronized (this) {
			readyToReceive = true;
			notifyAll();
		}
		component.waitUntil( p );
		synchronized (this) {
			readyToReceive = false;
			notifyAll();
		}
	}
	
}
