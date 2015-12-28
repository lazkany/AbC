/**
 * 
 */
package org.sysma.abc.core.NetworkMessages;

import java.io.Serializable;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Address;
import org.sysma.abc.core.centralized.ServerPortAddress;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MsgCentralized implements Serializable{
	private AbCMessage msg;
	private ServerPortAddress address;

	/**
	 * 
	 */
	public MsgCentralized(AbCMessage msg, ServerPortAddress address) {
		// TODO Auto-generated constructor stub
		this.msg=msg;
		this.address=address;
	}

	/**
	 * @return the msg
	 */
	public AbCMessage getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(AbCMessage msg) {
		this.msg = msg;
	}

	/**
	 * @return the address
	 */
	public ServerPortAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(ServerPortAddress address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MsgCentralized [msg=" + msg + ", address=" + address + "]";
	}
	

}
