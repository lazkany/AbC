/**
 * 
 */
package org.sysma.abc.core.topology;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.distributed.PortHandler;
/**
 * @author loreti
 *
 */
public class VirtualPort {

	private LinkedList<LocalPort> ports;
	private boolean running;
	private Lock portLock;
	
	public VirtualPort() {
		this.ports = new LinkedList<>();
		this.portLock = new ReentrantLock();
	}
	
	public AbCPort getPort() {
		LocalPort lp = new LocalPort();
		this.ports.add(lp);
		return lp;
	}
	
	public synchronized void start() {
		if (!running) {
			this.running = true;
		} else {
			throw new IllegalStateException("Local port already started!");
		}
	}

	public synchronized void stop() {
		if (running) {
			this.running = false;
		} else {
			throw new IllegalStateException("Local is not started!");
		}
	}
	

	public synchronized void dispatch(LocalPort source, AbCMessage message) {
		for (LocalPort p : ports) {
			if (p != source) {
				p.deliver(message);
			}
		}
	}
	
	public synchronized void close() {
		this.running = false;
	}
	
	public class LocalPort extends AbCPort {

		@Override
		protected void doStop() {
		}

		@Override
		protected void doStart() {
		}

		@Override
		protected PortHandler doConnect() {
			portLock.lock();
			return new PortHandler() {
				
				@Override
				public void send(AbCMessage message) {
					dispatch(LocalPort.this, message);
					portLock.unlock();
				}

				@Override
				public void send(NetworkPacket packet) {
					// TODO Auto-generated method stub
					
				}
				
			};
		}
		
	}
	
	public class LocalMessage {
		
		AbCMessage message;
		
		LocalPort source;
		
		public LocalMessage( AbCMessage message , LocalPort source ) {
			this.message = message;
			this.source = source;
		}
		
	}

	public boolean isRunning() {
		return running;
	}
}
