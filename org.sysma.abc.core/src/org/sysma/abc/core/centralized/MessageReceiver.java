/**
 */
package org.sysma.abc.core.centralized;

import java.io.IOException;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.MsgCentralized;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface MessageReceiver {

	//public void receiveMessage(AbCMessage m) throws InterruptedException, IOException;

	/**
	 * @param msgCentralized
	 */
	public void receiveMsg(MsgCentralized msgCentralized) throws InterruptedException, IOException;

}
