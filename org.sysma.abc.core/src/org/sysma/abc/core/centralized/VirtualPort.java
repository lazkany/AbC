
package org.sysma.abc.core.centralized;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.AbCPort;
import org.sysma.abc.core.Address;

public class VirtualPort extends AbCPort {

	private VirtualPortAddress address;

	public VirtualPort(int portId) {
		this.address = new VirtualPortAddress(portId);
		// isRunning=true; //CHANGE>>turn the port on
		// start(); //CHANGE>>start the port
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	protected void doSend(AbCMessage message) {
	}

}
