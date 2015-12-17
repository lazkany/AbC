/**
 */
package org.sysma.abc.core.centralized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.DuplicateNameException;

import com.google.gson.Gson;

public class ServerPort implements MessageReceiver {

	private static final int DEFAULT_SUBSCRIBE_PORT = 9999;
	private static final int DEFAULT_PROTOCOL_PORT = 9998;

	private Gson gson;
	protected HashMap<String, InetSocketAddress> clients;

	private ServerSocket subscribe_socket;
	private ServerSocket protocol_socket;

	public ServerPort() throws IOException {
		this(DEFAULT_SUBSCRIBE_PORT, DEFAULT_PROTOCOL_PORT);
	}

	public ServerPort(int subscribe_port, int protocol_port) throws IOException {
		this(subscribe_port, protocol_port, new HashMap<String, InetSocketAddress>());
	}

	public ServerPort(int subscribe_port, int protocol_port, HashMap<String, InetSocketAddress> clients)
			throws IOException {
		this.clients = clients;
		this.gson = AbCFactory.getGSon();
		this.subscribe_socket = new ServerSocket(subscribe_port);
		this.protocol_socket = new ServerSocket(protocol_port);
		new Thread(new SocketReceiver(protocol_socket, this)).start();
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

	private void dispatch(String clientName, AbCMessage message) throws IOException {
		InetSocketAddress clientAddress = clients.get(clientName);
		Socket socket = new Socket(clientAddress.getAddress(), clientAddress.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println(gson.toJson(message));
		writer.close();
		socket.close();
	}

	private synchronized void broadcast(AbCMessage message) {
		for (String clientName : clients.keySet()) {
			try {
				dispatch(clientName, message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveMessage(AbCMessage m) throws InterruptedException, IOException {

		broadcast(m);

	}

	public class RegistrationHandler implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Socket socket = subscribe_socket.accept();
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String command = reader.readLine();
					if ("REGISTER".equals(command)) {
						String name = reader.readLine();
						String host = reader.readLine();
						int port = Integer.parseInt(reader.readLine());
						try {
							register(name, new InetSocketAddress(host, port));
						} catch (DuplicateNameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if ("UNREGISTER".equals(command)) {
						String name = reader.readLine();
						unregister(name);
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
}
