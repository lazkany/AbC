package org.sysma.abc.core.topology.distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.DuplicateNameException;

import com.google.gson.Gson;

public class SocketReceiver implements Runnable {

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
	public SocketReceiver(ServerSocket ssocket, NetworkPacketReceiver receiver, ReceiverType type) {
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
				// if()
				NetworkPacket packet = gson.fromJson(reader, NetworkPacket.class);
				System.out.println(type);
				switch (type) {
				case CLIENT_RCV_SERVER:
					receiver.receive(packet);

					// client_rcv_server();
					break;
				case SERVER_RCV_CLIENT:
					 //System.out.println("i'm here");
					client_rcv_server((AbCServer) receiver, packet);
					break;
				case SERVER_RCV_SERVER:
					receiver.receive(packet);
					// server_rcv_server();
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

	public void client_rcv_server(AbCServer server, NetworkPacket packet) throws IOException {
		if (server.getParent() != null) {
			server.getCincoming().add(packet);
			// AbCServer other=(AbCServer)receiver;
			server.ForwardToParent(server.getParent(), packet, server.getPortId(), MsgType.REQUEST);
			
		} else if (server.getParent() == null && server.getServers().isEmpty()) {
			// System.out.println("");
			//System.out.println(String.valueOf(server.getCounter()));
			//System.out.println(String.valueOf(server.getCounter()));
			Signal(server, packet.getPacket().getSenderId());
			server.receive(packet);
		} else {
			packet.setId(String.valueOf(server.getCounter()));
			packet.setServerId(server.getPortId());
			 System.out.println(server.getPortId());
			Signal(server, packet.getPacket().getSenderId());
			server.receive(packet);
			server.forward(packet);
			
			
		}
		// System.out.println(server.getServers().isEmpty());
	}

	public void server_rcv_client(AbCServer receiver, NetworkPacket packet) {
	}

	public void server_rcv_server(AbCServer receiver, NetworkPacket packet) {
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

	public void Signal(AbCServer server, String name) throws IOException {
		// TODO: Correctly manage exceptions!
		InetSocketAddress clientAddress = server.getSignal_clients().get(name);
		Socket socket = new Socket(clientAddress.getAddress(), clientAddress.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println("ACK");
		writer.close();
		socket.close();
	}

}