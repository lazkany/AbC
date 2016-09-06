/**
 * 
 */
package org.sysma.abc.core.topology.distributed;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Queue;

import org.sysma.abc.core.NetworkMessages.NetworkPacket;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface NetworkPacketReceiver {
	public void receive(NetworkPacket packet);
	//public HashMap<String, InetSocketAddress> getClients();
	//public HashMap<String, InetSocketAddress> getSignal_clients();
	//public int getCounter();
	//public void setCounter(int c);
	//public Parent<String, InetSocketAddress> getParent();
	//public void setParent(Parent<String, InetSocketAddress> parent);
	//public Queue<NetworkPacket> getCincoming();
	//public void setCincoming(Queue<NetworkPacket> cincoming);
	//public Queue<NetworkPacket> getSincoming();
	//public void setSincoming(Queue<NetworkPacket> sincoming);
	//public String getPortId();
	//public void ForwardToParent(Parent<String, InetSocketAddress> parent, NetworkPacket message, String senderId,MsgType type) throws IOException;
}
