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
import java.util.HashMap;

import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.DuplicateNameException;

import com.google.gson.Gson;

public class AbCServer implements PacketReceiver {

	public static final int DEFAULT_SUBSCRIBE_PORT = 9999;
	public static final int DEFAULT_PROTOCOL_PORT = 9998;
	public static final String ERROR_MESSAGE = "ERROR";
	public static final String OK_MESSAGE = "OK";


	private Gson gson;
	protected HashMap<String, InetSocketAddress> clients;

	private ServerSocket subscribe_socket;
	private ServerSocket protocol_socket;

	private SocketReceiver receiver;
	private RegistrationHandler rHandler;
	private boolean running;
	
	public AbCServer() throws IOException {
		this(DEFAULT_SUBSCRIBE_PORT, DEFAULT_PROTOCOL_PORT);
	}

	public AbCServer(int subscribe_port, int protocol_port) throws IOException {
		this(subscribe_port, protocol_port, new HashMap<String, InetSocketAddress>());
	}

	public AbCServer(int subscribe_port, int protocol_port, HashMap<String, InetSocketAddress> clients)
			throws IOException {
		this.clients = clients;
		this.gson = AbCFactory.getGSon();
		this.subscribe_socket = new ServerSocket(subscribe_port);		
		this.protocol_socket = new ServerSocket(protocol_port);
		this.receiver = new SocketReceiver(protocol_socket, this);
		this.rHandler = new RegistrationHandler();
	}
	
	public void start() {
		Thread t1 = new Thread(receiver);
		t1.setDaemon(true);
		t1.start();
		Thread t2 = new Thread(rHandler);
		t2.setDaemon(true);
		t2.start();
		this.running = true;
	}

	public void register(String clientName, InetSocketAddress clientAddress) throws DuplicateNameException {
		if (clients.containsKey(clientName)) {
			throw new DuplicateNameException();
		}
		clients.put(clientName, clientAddress);
	}

	public void unregister(String clientName) {
		clients.remove(clientName);
	}

	private void dispatch(String clientName, AbCPacket message) throws IOException {
		InetSocketAddress clientAddress = clients.get(clientName);
		Socket socket = new Socket(clientAddress.getAddress(), clientAddress.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println(gson.toJson(message));
		writer.close();
		socket.close();
	}

	private synchronized void broadcast(AbCPacket msg) {
		for (String clientName : clients.keySet()) {
			if (!clientName.equals(msg.getSenderId())) {
				try {
					dispatch(clientName, msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void receive(AbCPacket msgCentralized) {
		// TODO Auto-generated method stub
		broadcast(msgCentralized);
	}

	public class RegistrationHandler implements Runnable {

		@Override
		public void run() {
			while (running) {
				try {
					//TODO: Correctly manage exceptions!
					System.out.println(
							"Waiting for subscriptions at " + subscribe_socket.getInetAddress().getCanonicalHostName()
									+ ":" + subscribe_socket.getLocalPort());
					Socket socket = subscribe_socket.accept();

					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					String command = reader.readLine();
					if ("REGISTER".equals(command)) {
						String name = reader.readLine();
						InetAddress host = InetAddress.getByName(reader.readLine());
						int port = Integer.parseInt(reader.readLine());
						try {

							register(name, new InetSocketAddress(host, port));
							writer.println(OK_MESSAGE);
							writer.println(protocol_socket.getLocalPort());
							writer.flush();
							System.out.println(name + " /" + host + ":" + port + " is registered");
							
						} catch (DuplicateNameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							writer.println(ERROR_MESSAGE);
							writer.flush();
						}
					}
					if ("UNREGISTER".equals(command)) {
						String name = reader.readLine();
						unregister(name);
						writer.println(OK_MESSAGE);
						writer.flush();
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

	public synchronized boolean isRunning() {
		return running;
	}

	public void stop() {
		running = false;
		receiver.stop();
		try {
			subscribe_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
