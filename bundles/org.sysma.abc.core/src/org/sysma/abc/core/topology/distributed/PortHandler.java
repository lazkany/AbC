/**
 * 
 */
package org.sysma.abc.core.topology.distributed;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;

/**
 * @author loreti
 *
 */
public interface PortHandler {
	
	public void send( AbCMessage message );
	public void send( NetworkPacket packet );

}
