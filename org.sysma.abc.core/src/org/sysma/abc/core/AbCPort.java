/**
 * 
 */
package org.sysma.abc.core;

import java.util.Hashtable;
import java.util.LinkedList;

import org.sysma.abc.core.NetworkMessages.MsgCentralized;
import org.sysma.abc.core.centralized.MessageReceiver;
import org.sysma.abc.core.exceptions.DuplicateNameException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCPort implements MessageReceiver {

	protected boolean isRunning;
	protected Hashtable<String, AbCComponent> nodes;
	protected LinkedList<AbCMessage> incomings;
	protected Thread portManager;

	/**
	 * Constructs a new <code>AbstractPort</code>.
	 */
	public AbCPort() {
		this.nodes = new Hashtable<String, AbCComponent>();
		this.incomings = new LinkedList<>();
		this.isRunning = false;
	}

	/**
	 * @param abCComponent
	 * @throws DuplicateNameException
	 */

	public void register(AbCComponent comp) throws DuplicateNameException {
		synchronized (nodes) {
			if (nodes.contains(comp.getName())) {
				throw new DuplicateNameException();
			}
			nodes.put(comp.getName(), comp);
		}
	}

	public synchronized void start() {
		this.isRunning = true;
		if (portManager == null) { // CHANGE>> changed the condition was "!="
			this.portManager = new Thread(new PortManager());
			this.portManager.start();
		}
	}

	public synchronized void stop() {
		this.isRunning = false;
		this.portManager = null;
	}

	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return
	 */
	public abstract Address getAddress();

	/**
	 * 
	 */
	protected synchronized void send(AbCMessage message) {
		incomings.add(message);
		doSend(message);
		notifyAll();
	}

	protected abstract void doSend(AbCMessage message);

	private class PortManager implements Runnable {

		@Override
		public void run() {
			while (isRunning) {
				AbCMessage message = nextMessage();
				if (message != null) {
					try {
						receiveMessage(message);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	protected synchronized AbCMessage nextMessage() {
		while (incomings.isEmpty() && isRunning) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		if (!isRunning) {
			return null;
		}
		return incomings.poll();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sysma.abc.core.centralized.MessageReceiver#receiveMessage(org.sysma.
	 * abc.core.AbCMessage)
	 */

	public void receiveMessage(AbCMessage message) throws InterruptedException {
		// TODO Auto-generated method stub
		handleMessage(message);
	}
	public void receiveMsg(MsgCentralized message) throws InterruptedException {
		// TODO Auto-generated method stub
		AbCMessage msg=message.getMsg();
		receiveMessage(msg);
	}
	protected void handleMessage(AbCMessage message) throws InterruptedException {
		synchronized (nodes) {
			for (AbCComponent c : nodes.values()) {
				if ((message.getSender() != c) && (message.isAReceiverFor(c.getStore()))) {
					c.receive(message);
				}
			}
		}
	}

}
