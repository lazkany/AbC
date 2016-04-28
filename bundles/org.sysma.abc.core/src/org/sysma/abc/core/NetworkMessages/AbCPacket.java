/**
 * 
 */
package org.sysma.abc.core.NetworkMessages;

import java.io.Serializable;

import org.sysma.abc.core.AbCMessage;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AbCPacket implements Serializable{
	
	private AbCMessage msg;
	private String senderId;

	/**
	 * 
	 */
	public AbCPacket(AbCMessage msg, String senderId) {
		// TODO Auto-generated constructor stub
		this.msg=msg;
		this.senderId=senderId;
	}

	/**
	 * @return the msg
	 */
	public AbCMessage getMessage() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMessage(AbCMessage msg) {
		this.msg = msg;
	}

	/**
	 * @return the address
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param address the address to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MsgCentralized [msg=" + msg + ", from=" + senderId + "]";
	}

	@Override
	public int hashCode() {
		return this.msg.hashCode()+this.senderId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbCPacket) {
			AbCPacket other = (AbCPacket) obj;
			return this.msg.equals(other.msg)&&this.senderId.equals(other.senderId);
		}
		return false;
	}
	

}
