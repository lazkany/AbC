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
				Socket s = ssocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				NetworkPacket packet = gson.fromJson(reader, NetworkPacket.class);
				reader.close();
				s.close();
				switch (type) {
				case CLIENT_RCV_SERVER:
					//System.out.println(packet);
					receiver.receive(packet);
					break;
				case SERVER_RCV_CLIENT:
					
					System.out.println(packet);
					server_rcv_client((AbCServer) receiver, packet);
					break;
				case SERVER_RCV_SERVER:
					//if (packet.getType() == MsgType.DATA)
						System.out.println(packet);
					server_rcv_server((AbCServer) receiver, packet);
					break;
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void server_rcv_client(AbCServer receiver, NetworkPacket packet) throws IOException {
		System.out.println("MY COUNTER IS "+receiver.Counter());
		if (receiver.getParent() != null) {
			// receiver.getCincoming().add(packet);
			// AbCServer other=(AbCServer)receiver;
			//System.out.println("ask for ID");
			receiver.ForwardToParent(receiver, packet, MsgType.REQUEST);
			//System.out.println(packet);
		}
		
		// else if (receiver.getParent() == null &&
		// receiver.getServers().isEmpty()) {
		// System.out.println("Deliver to clients");
		// Signal(receiver, packet.getPacket().getSenderId());
		// receiver.receive(packet);
		// }
		else {
			packet.setId(String.valueOf(receiver.getCounter()));
			packet.setServerId(receiver.getPortId());
			//System.out.println("ACK, Receiver and Forward");
//			System.out.println(
//					"I AM ROOT and the message is originated from my clients, i also have connected servers MY COUNTER="
//							+ receiver.getCounter());
//			System.out.println(packet);
			Signal(receiver, packet.getPacket().getSenderId());
			packet.setType(MsgType.DATA);
			receiver.receive(packet);
			receiver.forward(packet, receiver.getPortId());
//			for(NetworkPacket p:receiver.getQueue())
//			{
//				System.out.print(p.getId()+" > ");
//			}
//			System.out.println("");
			System.out.println("line123 "+"my counter is=> "+receiver.Counter());

		}
	}

	public void server_rcv_server(AbCServer receiver, NetworkPacket packet) throws IOException, InterruptedException {
		System.out.println("MY COUNTER IS "+receiver.Counter());
		switch (packet.getType()) {
		case REQUEST:
			if (receiver.getParent() != null) {
				//System.out.println("Request: send to parent");
				receiver.ForwardToParent(receiver, packet, MsgType.REQUEST);
			} else {
				packet.setId(String.valueOf(receiver.getCounter()));
				//System.out.println("Root=>REPLY ONLY TO REQUESTER a Reply packet: " + packet.getId());
				receiver.RootReply(receiver, packet, MsgType.REPLY);
//				for(NetworkPacket p:receiver.getQueue())
//				{
//					System.out.print(p.getId()+" > ");
//				}
//				System.out.println("");
				System.out.println("line144 "+"my counter is=> "+receiver.Counter());
				// receiver.receive(packet);
			}
			break;
		case REPLY:
			if (receiver.clients.containsKey(packet.getPacket().getSenderId())) {
//				System.out.println("Reply=>DATA: The message is originated by my clients,but I only receive it if"
//						+ (Integer.parseInt(packet.getId()) == receiver.Counter() + 1));
				packet.setServerId(receiver.getPortId());
				packet.setType(MsgType.DATA);
				if (Integer.parseInt(packet.getId()) == receiver.Counter() + 1) {
					int c = Integer.parseInt(packet.getId());
					receiver.setCounter(c);
//					System.out.println(
//							"REPLY=>DATA: I AM AN END-POINT SERVER and updated my counter=>" + receiver.Counter());
					Signal(receiver, packet.getPacket().getSenderId());
					
					receiver.receive(packet);
					receiver.forward(packet, receiver.getPortId());
					receiver.ForwardToParent(receiver, packet, MsgType.DATA);
//					for(NetworkPacket p:receiver.getQueue())
//					{
//						System.out.print(p.getId()+" > ");
//					}
//					System.out.println("");
					System.out.println("line169 "+"my counter is=> "+receiver.Counter());
				} else {
					//System.out.println("The packet is mine and out of order=>delayed packet: " + packet);
					receiver.delayPacket(packet);
					receiver.setCounter(-404);
//					for(NetworkPacket p:receiver.getQueue())
//					{
//						System.out.print(p.getId()+" > ");
//					}
//					System.out.println("");
					System.out.println("line178 "+"my counter is=> "+receiver.Counter());
				}

			} else {
				//System.out.println("Reply: forward reply because the msg is not originated from my clients");
				packet.setServerId(receiver.getPortId());
				receiver.forward(packet, receiver.getPortId());
//				for(NetworkPacket p:receiver.getQueue())
//				{
//					System.out.print(p.getId()+" > ");
//				}
//				System.out.println("");
				System.out.println("line190 "+"my counter is=> "+receiver.Counter());
			}
			break;
		case DATA:
			if (Integer.parseInt(packet.getId()) > (receiver.Counter() + 1)) {
//				System.out.println("my counter: " + receiver.Counter() + " while msg id is " + packet.getId()
//						+ " Data: The message is delayed");
				receiver.delayPacket(packet);
				receiver.setCounter(-404);
//				for(NetworkPacket p:receiver.getQueue())
//				{
//					System.out.print(p.getId()+" > ");
//				}
//				System.out.println("");
				System.out.println("line203 "+"my counter is=> "+receiver.Counter());
			} else {
				//System.out.println("Data: message is ordered, update your counter unless you are the root");
				if (receiver.parent == null) {
//					System.out.println(
//							"Data Forward: I AM THE ROOT and i don't update and my counter=" + receiver.Counter());
//					System.out.println(packet);
//					for(NetworkPacket p:receiver.getQueue())
//					{
//						System.out.print(p.getId()+" > ");
//					}
//					System.out.println("");
					System.out.println("line215 "+"my counter is=> "+receiver.Counter());
				}
				String name = packet.getServerId();
				packet.setServerId(receiver.getPortId());
				if (receiver.parent != null) {
					//System.out.println("the sender is my parent=>" + receiver.parent.getKey().equals(name));
					receiver.setCounter(Integer.parseInt(packet.getId()));
					//System.out.println("Data: I AM A NORMAL SERVER and updated my counter=>" + receiver.Counter());
					if (!receiver.parent.getKey().equals(name))
						receiver.ForwardToParent(receiver, packet, MsgType.DATA);
				}
				receiver.forward(packet, name);
				receiver.receive(packet);
//				for(NetworkPacket p:receiver.getQueue())
//				{
//					System.out.print(p.getId()+" > ");
//				}
//				System.out.println("");
				System.out.println("line233 "+"my counter is=> "+receiver.Counter());
			}
			break;
		case EMPTY:
			System.out.println("This case is not possible");
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