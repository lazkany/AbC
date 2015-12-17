/**
 */
package org.sysma.abc.core.centralized;

import java.io.IOException;

import org.sysma.abc.core.AbCMessage;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface MessageReceiver {

	public void receiveMessage(AbCMessage m) throws InterruptedException, IOException;

}
