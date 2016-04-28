/**
 */
package org.sysma.abc.core.topology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.AbCPortException;

import com.google.gson.Gson;

/**
 * 
 * 
 * 
 * @author loreti
 *
 */
public class AbCClient extends AbCPort implements PacketReceiver {

	private final String portId;
	private final Gson gson;
	private final InetAddress localAddress;
	private final int localPort;
	private ServerSocket serverSocket;
	private SocketReceiver receiver;
	private boolean registered;
	private int dataPort;
	private InetAddress serverAddress;
	private int commandPort;
	
	public AbCClient(InetAddress localAddress , int port) {
		this.localAddress = localAddress;
		this.localPort = port;
		this.portId = this.localAddress.getHostAddress()+"@"+this.localPort;
		this.gson = AbCFactory.getGSon();
	}
	
	

	@Override
	protected PortHandler doConnect() {

		Socket socket;
		
		try {
			socket = new Socket(serverAddress , dataPort);

			return new PortHandler() {
				
				@Override
				public void send(AbCMessage message) {
					try {
						AbCPacket msgCentralized=new AbCPacket(message, portId);
						PrintWriter writer = new PrintWriter(socket.getOutputStream());
						//System.out.println(gson.toJson(msgCentralized));
						writer.println(gson.toJson(msgCentralized));
						writer.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			};

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}


	/*
	private InetSocketAddress serverAddress;
	private SocketReceiver receiver;
	 */
	
	public void register( InetAddress serverAddress , int commandPort ) throws IOException {
		//TODO: Correctly manage exceptions!
		Socket socket = new Socket();
		socket.bind(new InetSocketAddress(this.localAddress, 0));
		socket.connect(new InetSocketAddress(serverAddress, commandPort));
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer.println("REGISTER\n"+portId+"\n"+socket.getLocalAddress().getCanonicalHostName()+"\n"+this.localPort);
//			writer.println("REGISTER\n"+portId+"\n"+this.localAddress.getInetAddress().getHostAddress()+"\n"+this.localAddress.getLocalPort());
		writer.flush();
		String code = reader.readLine();
		if (AbCServer.OK_MESSAGE.equals(code)) {
			this.commandPort = commandPort;
			this.dataPort = Integer.parseInt( reader.readLine() );
			this.serverAddress = serverAddress;
			this.registered = true;
		}
		reader.close();
		writer.close();		
		socket.close();
		
	}

	@Override
	protected void doStop() {
		receiver.stop();
	}

	@Override
	protected void doStart() throws AbCPortException {
		if (!registered) {
			throw new IllegalStateException("This port is not registered at any server!");
		}
		try {
			this.serverSocket = new ServerSocket();
			this.serverSocket.bind(new InetSocketAddress(this.localAddress,this.localPort));
			this.receiver = new SocketReceiver(serverSocket, this);
			Thread t = new Thread(receiver);
			t.setDaemon(true);
			t.start();		
		} catch (IOException e) {
			throw new AbCPortException(e);
		}
	}

	@Override
	public void receive(AbCPacket packet) {
		deliver(packet.getMessage());
	}
	
	
//	private InetAddress clientToServerAddress( ) {
//		InetAddress address = localAddress.getInetAddress();
//		if (address.isAnyLocalAddress()) {
//			if (serverAddress.getAddress().isAnyLocalAddress()||serverAddress.getAddress().isLoopbackAddress()) {
//				return InetAddress.getLoopbackAddress().getCanonicalHostName();
//			} else {
//				return address.getHostAddress();
//			}
//		}
//	}
	
}
