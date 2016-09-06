/**
 * 
 */
package org.sysma.abc.core.NetworkMessages;

import org.sysma.abc.core.topology.distributed.MsgType;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class NetworkPacket {
	//private String incomingLink;
	private String serverId;
	private String id;
	private MsgType type;
	private AbCPacket packet;

	public NetworkPacket(AbCPacket packet) {
		this("","",MsgType.EMPTY,packet);
	}

	public NetworkPacket(String serverId, String id, MsgType type, AbCPacket packet) {

		//this.incomingLink = incomingLink;
		this.serverId = serverId;
		this.id = id;
		this.type = type;
		this.packet = packet;
	}

//	public String getIncomingLink() {
//		return incomingLink;
//	}
//
//	public void setIncomingLink(String incomingLink) {
//		this.incomingLink = incomingLink;
//	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MsgType getType() {
		return type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}

	public AbCPacket getPacket() {
		return packet;
	}

	public void setPacket(AbCPacket packet) {
		this.packet = packet;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((packet == null) ? 0 : packet.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NetworkPacket))
			return false;
		NetworkPacket other = (NetworkPacket) obj;
		if (packet == null) {
			if (other.packet != null)
				return false;
		} else if (!packet.equals(other.packet))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NetworkPacket [serverId=" + serverId + ", id=" + id + ", type="
				+ type + ", packet=" + packet + "]";
	}
	
}
