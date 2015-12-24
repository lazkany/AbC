/**
 */
package org.sysma.abc.core.centralized;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.AbCPort;
import org.sysma.abc.core.Address;
import org.sysma.abc.core.abcfactoy.AbCFactory;

import com.google.gson.Gson;

public class ServerPortClient extends AbCPort {

	private ServerPortAddress serverAddress;
	private ServerSocket localAddress;
	private Gson gson;

	public ServerPortClient(ServerPortAddress serverAddress, ServerSocket localAddress) {
		this.serverAddress = serverAddress;
		this.localAddress = localAddress;
		this.gson = AbCFactory.getGSon();
		Thread t = new Thread(new SocketReceiver(this.localAddress, this));
		t.setDaemon(true);
		t.start();
	}

	@Override
	protected void doSend(AbCMessage message) {
		InetSocketAddress isc = serverAddress.getAddress();
		Socket socket;
		try {
			socket = new Socket(isc.getAddress(), isc.getPort());
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println(gson.toJson(message));
			writer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Address getAddress() {
		return serverAddress;
	}

	/**
	 * @return the localAddress
	 */
	public ServerSocket getLocalAddress() {
		return localAddress;
	}

	
	public void RemoteRegister(ServerPortAddress serverAdd) {
		InetSocketAddress isc = serverAdd.getAddress();
		Socket socket;
		try {
			socket = new Socket(isc.getAddress(), isc.getPort());
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println("REGISTER\n"+"Node"+this.getLocalAddress().getLocalPort()+"\n"+"0.0.0.0\n"+this.getLocalAddress().getLocalPort());
			writer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
