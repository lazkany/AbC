/**
 * 
 */
package org.sysma.abc.core.topology.distributed;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCPort {

	protected boolean isRunning;

	private MessageReceiver component;
	//private NetworkPacketReceiver server;
	/**
	 * Constructs a new <code>AbCPort</code>.
	 */
	public AbCPort() {
		this.isRunning = false;
	}
	
	public final void start() throws AbCPortException {
		if (isRunning) {
			throw new IllegalStateException();
		}
		doStart();
		this.isRunning = true;
	}
	
	public final void stop() throws AbCPortException {
		if (isRunning) {
			doStop();
		} else {
			throw new IllegalStateException();
		}
		this.isRunning = false;
	}

	protected abstract void doStop() throws AbCPortException;

	protected abstract void doStart() throws AbCPortException;
	
	/**
	 * @param abCComponent
	 * @throws DuplicateNameException
	 */
	public void setReceiver(MessageReceiver component) {
		this.component = component;
	}
//	public void setReceiver(NetworkPacketReceiver server) {
//		this.server = server;
//	}
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * 
	 */
	public PortHandler connect() {
		if (isRunning) {
			return doConnect();
		} else {
			throw new IllegalStateException();
		}
	}

	protected abstract PortHandler doConnect();

	public void deliver( AbCMessage message ) {
		if (this.component != null) {
			this.component.receive(message);
		}
	}
	
	public void deliver( NetworkPacket packet ) {
//		if (this.server != null) {
//			this.server.receive(packet);
//		}
		if (this.component != null) {
			this.component.receive(packet.getPacket().getMessage());
		}
	}


}
