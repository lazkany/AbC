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
				//System.out.println("Waiting for messages at " + ssocket.getInetAddress().getCanonicalHostName() + ":"
				//		+ ssocket.getLocalPort());
				Socket s = ssocket.accept();
				// PrintWriter writer = new PrintWriter(s.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				NetworkPacket packet = gson.fromJson(reader, NetworkPacket.class);
				reader.close();
				s.close();
				switch (type) {
				case CLIENT_RCV_SERVER:
					
					receiver.receive(packet);
					break;
				case SERVER_RCV_CLIENT:
					//System.out.println("Server received from client packet " + packet.getId() + " is received");
					server_rcv_client((AbCServer) receiver, packet);
					break;
				case SERVER_RCV_SERVER:
					// receiver.receive(packet);
					//System.out.println("Server received from server packet " + packet.getId() + " is received");
					server_rcv_server((AbCServer) receiver, packet);
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// public void client_rcv_server(AbCServer receiver, NetworkPacket packet) {
	// }
	public void server_rcv_client(AbCServer receiver, NetworkPacket packet) throws IOException {
		if (receiver.getParent() != null) {
			receiver.getCincoming().add(packet);
			// AbCServer other=(AbCServer)receiver;
			receiver.ForwardToParent(receiver, packet, MsgType.REQUEST);

		} else if (receiver.getParent() == null && receiver.getServers().isEmpty()) {
			Signal(receiver, packet.getPacket().getSenderId());
			receiver.receive(packet);
		} else {
			packet.setId(String.valueOf(receiver.getCounter()));
			packet.setServerId(receiver.getPortId());
			// System.out.println(receiver.getPortId());
			Signal(receiver, packet.getPacket().getSenderId());
			packet.setType(MsgType.DATA);
			//System.out.println("client received packet " + packet.getId() + " is received");
			receiver.receive(packet);
			receiver.forward(packet, receiver.getPortId());

		}
	}

	public void server_rcv_server(AbCServer receiver, NetworkPacket packet) throws IOException {

		switch (packet.getType()) {
		case REQUEST:
			if (receiver.getParent() != null) {
				receiver.getCincoming().add(packet);
				receiver.ForwardToParent(receiver, packet, MsgType.REQUEST);
			} else {
				packet.setId(String.valueOf(receiver.getCounter()));
				receiver.RootReply(receiver, packet, MsgType.REPLY);
				//System.out.println("packet " + packet.getId() + " is received");
				receiver.receive(packet);
			}
			break;
		case REPLY:
			if (receiver.clients.containsKey(packet.getPacket().getSenderId())) {
				int c=Integer.parseInt(packet.getId());
				receiver.setCounter(c);
				Signal(receiver, packet.getPacket().getSenderId());
				packet.setServerId(receiver.getPortId());
				packet.setType(MsgType.DATA);
				receiver.receive(packet);
				
				receiver.forward(packet, receiver.getPortId());
				receiver.ForwardToParent(receiver, packet, MsgType.DATA);

			}
			// else if(receiver.getCincoming().contains(packet.getPacket())) {
			else {
				int c=Integer.parseInt(packet.getId());
				receiver.setCounter(c);
				packet.setServerId(receiver.getPortId());
				receiver.forward(packet, receiver.getPortId());
			}
			// receiver.getCincoming().removeIf(c->c.getPacket().getSenderId().equals(packet.getPacket().getSenderId()));
			// receiver.getCincoming().

			break;
		case DATA:
			String name=packet.getServerId();
			packet.setServerId(receiver.getPortId());
			receiver.forward(packet, name);
			receiver.receive(packet);
			if(receiver.parent!=null &&!receiver.parent.getKey().equals(name))
			receiver.ForwardToParent(receiver, packet, MsgType.DATA);
			//System.out.println("client received packet " + packet.getId() + " is received");
			break;
		case EMPTY:

			break;
		}
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