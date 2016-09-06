/**
 * 
 */
package org.sysma.abc.core.topology.distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;

import com.google.gson.Gson;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class SignalingSocketReceiver implements Runnable{
	/**
	 * 
	 */
	private ServerSocket ssocket;

	private Gson gson = AbCFactory.getGSon();

	private NetworkPacketReceiver receiver;

	private boolean isRunning = true;
	private ReceiverType type;

	/**
	 * @param socketPort
	 */
	public SignalingSocketReceiver(ServerSocket ssocket, NetworkPacketReceiver receiver, ReceiverType type) {
		this.ssocket = ssocket;
		this.receiver = receiver;
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public ReceiverType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ReceiverType type) {
		this.type = type;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				System.out.println("Waiting for messages at " + ssocket.getInetAddress().getCanonicalHostName() + ":"
						+ ssocket.getLocalPort());
				Socket s = ssocket.accept();
				PrintWriter writer = new PrintWriter(s.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// AbCMessage msg = gson.fromJson(reader, AbCMessage.class);
				NetworkPacket packet = gson.fromJson(reader, NetworkPacket.class);
				System.out.println(type);
				switch (type) {
				case CLIENT_RCV_SERVER:
					receiver.receive(packet);
					client_rcv_server();
					break;
				case SERVER_RCV_CLIENT:
					writer.println("ACK");
					receiver.receive(packet);
					server_rcv_client();
					break;
				case SERVER_RCV_SERVER:
					receiver.receive(packet);
					server_rcv_server();
					break;
				}

				reader.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void client_rcv_server() {
	}
	public void server_rcv_client() {
	}
	public void server_rcv_server() {
	}
	public void stop() {
		try {
			this.isRunning = false;
			this.ssocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
