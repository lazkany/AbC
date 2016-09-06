/**
 */
package org.sysma.abc.core.topology.distributed;

import java.io.IOException;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface PacketReceiver {

	//public void receiveMessage(AbCMessage m) throws InterruptedException, IOException;

	/**
	 * @param msgCentralized
	 */
	public void receive(AbCPacket packet);

}
