/**
 */
package org.sysma.abc.core.topology.distributed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCServer.RegistrationHandler;

import com.google.gson.Gson;

/**
 * @author Yehia Abd Alrahman
 * @author loreti
 */
public class AbCClient extends AbCPort implements NetworkPacketReceiver {
	private final String portId;
	private final Gson gson;
	private final InetAddress localAddress;
	private final int localPort;
	private int signal_port;
	private ServerSocket serverSocket;
	private SignalHandler sigHandler;
	private SocketReceiver receiver;
	private ServerSocket signal_socket;
	private boolean registered;
	private int dataPort;
	private InetAddress serverAddress;
	private int commandPort;
	public String ACK_MESSAGE = "";

	public AbCClient(InetAddress localAddress, int port, int sig_Port) throws IOException {
		this.localAddress = localAddress;
		this.localPort = port;
		this.signal_port = sig_Port;
		this.signal_socket = new ServerSocket(signal_port);
		this.sigHandler = new SignalHandler(signal_socket);
		this.portId = this.localAddress.getHostAddress() + "@" + this.localPort;
		this.gson = AbCFactory.getGSon();
	}

	/**
	 * @return the portId
	 */
	public String getPortId() {
		return portId;
	}

	@Override
	protected PortHandler doConnect() {

		Socket socket;

		try {
			socket = new Socket(serverAddress, dataPort);

			return new PortHandler() {

				@Override
				public void send(AbCMessage message) {
					try {
						NetworkPacket msg = new NetworkPacket(new AbCPacket(message, portId));
						PrintWriter writer = new PrintWriter(socket.getOutputStream());
						 //System.out.println("want to send: "+msg);
						writer.println(gson.toJson(msg));
						writer.close();
						socket.close();
						nAckMsg();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void send(NetworkPacket packet) {
					//
				}

			};

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * private InetSocketAddress serverAddress; private SocketReceiver receiver;
	 */
	public synchronized void nAckMsg() {
		while (ACK_MESSAGE.equals("")) {
			try {
				wait();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ACK_MESSAGE = "";

	}

	public synchronized void ack() {
		ACK_MESSAGE = "ack";
		notifyAll();
		// ACK_MESSAGE=null;
	}

	public void register(InetAddress serverAddress, int commandPort) throws IOException {
		// TODO: Correctly manage exceptions!
		Socket socket = new Socket();
		socket.bind(new InetSocketAddress(this.localAddress, 0));
		socket.connect(new InetSocketAddress(serverAddress, commandPort));
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer.println("REGISTER\n" + portId + "\n" + socket.getLocalAddress().getCanonicalHostName() + "\n"
				+ this.localPort + "\n" + this.signal_port);
		// writer.println("REGISTER\n"+portId+"\n"+this.localAddress.getInetAddress().getHostAddress()+"\n"+this.localAddress.getLocalPort());
		writer.flush();
		String code = reader.readLine();
		if (AbCServer.OK_MESSAGE.equals(code)) {
			this.commandPort = commandPort;
			this.dataPort = Integer.parseInt(reader.readLine());
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
			this.serverSocket.bind(new InetSocketAddress(this.localAddress, this.localPort));
			this.receiver = new SocketReceiver(serverSocket, this, ReceiverType.CLIENT_RCV_SERVER);
			Thread t = new Thread(receiver);
			//t.setDaemon(true);
			t.start();
			Thread t2 = new Thread(sigHandler);
			//t2.setDaemon(true);
			t2.start();
		} catch (IOException e) {
			throw new AbCPortException(e);
		}
	}

	@Override
	public void receive(NetworkPacket packet) {
		deliver(packet);
	}

	public class SignalHandler implements Runnable {
		ServerSocket svr;

		public SignalHandler(ServerSocket svr) {
			this.svr = svr;
		}

		@Override
		public void run() {
			while (isRunning) {
				try {
					Socket socket = svr.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String command = reader.readLine();
					//System.out.println(command);
					if ("ACK".equals(command)) {
						ack();
					}
					reader.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#getClients(
	 * )
	 */
//	@Override
//	public HashMap<String, InetSocketAddress> getClients() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#
//	 * getSignal_clients()
//	 */
//	@Override
//	public HashMap<String, InetSocketAddress> getSignal_clients() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#getCounter()
//	 */
//	@Override
//	public int getCounter() {
//		// TODO Auto-generated method stub
//		return -1;
//	}
//
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#setCounter(int)
//	 */
//	@Override
//	public void setCounter(int c) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#getParent()
//	 */
//	@Override
//	public Parent<String, InetSocketAddress> getParent() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#setParent(org.sysma.abc.core.topology.distributed.Parent)
//	 */
//	@Override
//	public void setParent(Parent<String, InetSocketAddress> parent) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#getCincoming()
//	 */
//	@Override
//	public Queue<NetworkPacket> getCincoming() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#setCincoming(java.util.Queue)
//	 */
//	@Override
//	public void setCincoming(Queue<NetworkPacket> cincoming) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#getSincoming()
//	 */
//	@Override
//	public Queue<NetworkPacket> getSincoming() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#setSincoming(java.util.Queue)
//	 */
//	@Override
//	public void setSincoming(Queue<NetworkPacket> sincoming) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see org.sysma.abc.core.topology.distributed.NetworkPacketReceiver#ForwardToParent(org.sysma.abc.core.topology.distributed.Parent, org.sysma.abc.core.NetworkMessages.NetworkPacket)
//	 */
//	@Override
//	public void ForwardToParent(Parent<String, InetSocketAddress> parent, NetworkPacket message, String senderId,MsgType type) throws IOException {
//		// TODO Auto-generated method stub
//		
//	}
}
