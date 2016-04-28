/**
 */
package org.sysma.abc.core.topology;

import java.io.IOException;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface MessageReceiver {

	//public void receiveMessage(AbCMessage m) throws InterruptedException, IOException;

	/**
	 * @param msgCentralized
	 */
	public void receive(AbCMessage message);

}
