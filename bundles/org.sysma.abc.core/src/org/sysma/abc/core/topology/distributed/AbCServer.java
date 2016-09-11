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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.distributed.PortHandler;

import com.google.gson.Gson;

/**
 * @author Yehia Abd Alrahman
 * @author loreti
 */
public class AbCServer implements NetworkPacketReceiver {

	public static final int DEFAULT_cSub_PORT = 9999;
	public static final int DEFAULT_csData_PORT = 9998;
	public static final int DEFAULT_sSub_PORT = 9997;
	public static final int DEFAULT_ssData_PORT = 9996;
	public static final String ERROR_MESSAGE = "ERROR";
	public static final String OK_MESSAGE = "OK";
	public static final String ACK_MESSAGE = "ACK";
	private final String portId;
	private Gson gson;
	protected HashMap<String, InetSocketAddress> clients;
	protected HashMap<String, InetSocketAddress> signal_clients;
	protected HashMap<String, InetSocketAddress> servers;
	protected Parent<String, InetSocketAddress> parent = null;
	private ServerSocket cSubscribe_socket;
	private ServerSocket csData_socket;
	private ServerSocket ssData_socket;
	private ServerSocket sSubscribe_socket;
	private SocketReceiver cReceiver;
	private SocketReceiver sReceiver;
	private RegistrationHandler cHandler;
	private RegistrationHandler sHandler;
	private DataHandler dHandler;
	private boolean flag=true;
	private int csData_port;
	private int ssData_port;
	private InetAddress parentAdress;
	private boolean registered;
	private boolean running;

	/*-----------------------------------*/
	private int counter = 0;
	private int lastId = -1;
	private Queue<NetworkPacket> cincoming;
	private Queue<NetworkPacket> sincoming;
	private Comparator<NetworkPacket> comparator = new PacketComparator();
	private PriorityQueue<NetworkPacket> queue = new PriorityQueue<NetworkPacket>(100000, comparator);

	public AbCServer() throws IOException {
		this(DEFAULT_cSub_PORT, DEFAULT_csData_PORT, DEFAULT_sSub_PORT, DEFAULT_ssData_PORT);
	}

	public AbCServer(int sSub_port, int ssData_port) throws IOException {
		this(DEFAULT_cSub_PORT, DEFAULT_csData_PORT, sSub_port, ssData_port);
	}

	public AbCServer(int cSub_port, int csData_port, int sSub_port, int ssData_port) throws IOException {
		this(cSub_port, csData_port, sSub_port, ssData_port, new HashMap<String, InetSocketAddress>(),
				new HashMap<String, InetSocketAddress>(), new HashMap<String, InetSocketAddress>());
	}

	public AbCServer(int cSub_port, int csData_port, int sSub_port, int ssData_port,
			HashMap<String, InetSocketAddress> clients, HashMap<String, InetSocketAddress> servers,
			HashMap<String, InetSocketAddress> sclients) throws IOException {
		this.clients = clients;
		this.signal_clients = sclients;
		this.servers = servers;
		this.gson = AbCFactory.getGSon();
		this.cSubscribe_socket = new ServerSocket(cSub_port);
		this.csData_socket = new ServerSocket(csData_port);
		this.sSubscribe_socket = new ServerSocket(sSub_port);
		this.ssData_socket = new ServerSocket(ssData_port);
		this.cReceiver = new SocketReceiver(csData_socket, this, ReceiverType.SERVER_RCV_CLIENT);
		this.sReceiver = new SocketReceiver(ssData_socket, this, ReceiverType.SERVER_RCV_SERVER);
		this.cHandler = new RegistrationHandler(cSubscribe_socket);
		this.sHandler = new RegistrationHandler(sSubscribe_socket);
		this.dHandler = new DataHandler();
		this.portId = this.ssData_socket.getInetAddress().getHostAddress() + "@" + this.ssData_socket.getLocalPort();
		this.cincoming = new LinkedList<>();
		this.sincoming = new LinkedList<>();
	}

	/**
	 * @return the queue
	 */
	public PriorityQueue<NetworkPacket> getQueue() {
		return queue;
	}
	public synchronized void delayPacket(NetworkPacket packet){
		queue.add(packet);
		notifyAll();
	}
	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(PriorityQueue<NetworkPacket> queue) {
		this.queue = queue;
	}

	/**
	 * @return the servers
	 */
	public HashMap<String, InetSocketAddress> getServers() {
		return servers;
	}

	/**
	 * @param servers
	 *            the servers to set
	 */
	public void setServers(HashMap<String, InetSocketAddress> servers) {
		this.servers = servers;
	}

	/**
	 * @return the clients
	 */
	public HashMap<String, InetSocketAddress> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 *            the clients to set
	 */
	public void setClients(HashMap<String, InetSocketAddress> clients) {
		this.clients = clients;
	}

	/**
	 * @return the signal_clients
	 */
	public HashMap<String, InetSocketAddress> getSignal_clients() {
		return signal_clients;
	}

	/**
	 * @param signal_clients
	 *            the signal_clients to set
	 */
	public void setSignal_clients(HashMap<String, InetSocketAddress> signal_clients) {
		this.signal_clients = signal_clients;
	}

	/**
	 * @return the cincoming
	 */
	public Queue<NetworkPacket> getCincoming() {
		return cincoming;
	}

	/**
	 * @param cincoming
	 *            the cincoming to set
	 */
	public void setCincoming(Queue<NetworkPacket> cincoming) {
		this.cincoming = cincoming;
	}

	/**
	 * @return the sincoming
	 */
	public Queue<NetworkPacket> getSincoming() {
		return sincoming;
	}

	/**
	 * @param sincoming
	 *            the sincoming to set
	 */
	public void setSincoming(Queue<NetworkPacket> sincoming) {
		this.sincoming = sincoming;
	}

	/**
	 * @return the counter
	 */
	public synchronized int getCounter() {
		return ++counter;
	}

	public synchronized int Counter() {
		return counter;
	}

	public synchronized int _Counter() throws InterruptedException, IOException {
		while (lastId == -1 || queue.isEmpty()
				|| !(queue.isEmpty()) && (Integer.parseInt(queue.peek().getId())>counter+1)) {
			wait();
		}
//		while (lastId == -1 || queue.isEmpty()) {
//			wait();
//		}
		flag=false;
		//Queue<NetworkPacket> Copyqueue = new LinkedList<>(queue);
		//Copyqueue=queue;
		//int x=Integer.parseInt(Copyqueue.peek().getId())+1;
	//	while((Integer.parseInt(queue.peek().getId()))<=counter+1)
		for(NetworkPacket pckt:queue)
		{
			if((Integer.parseInt(pckt.getId())<=counter+1)){
				queue.remove(pckt);
				counter=Integer.parseInt(pckt.getId());
				String name = pckt.getServerId();
				
				counter=Integer.parseInt(pckt.getId());
				System.out.println("the packet: " + pckt.getId() + " is freed AND MY COUNTER is "+counter);
			//	notifyAll();
				if (!parent.getKey().equals(name)) {
					ForwardToParent(this, pckt, MsgType.DATA);
					pckt.setServerId(getPortId());
				}
				pckt.setServerId(getPortId());
				forward(pckt, name);
				if(clients.containsKey(pckt.getPacket().getSenderId()))
				Signal(this, pckt.getPacket().getSenderId());
				receive(pckt);
			}
		}
		{
			
			//NetworkPacket packet = queue.remove();
			
			
			
			
		}
		System.out.println("the remaining are:");
		for(NetworkPacket p:queue)
		{
			System.out.print(p.getId()+" > ");
		}
		System.out.println("");
		lastId = -1;
		flag=true;
		notifyAll();
		return counter;

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
	/**
	 * @param counter
	 *            the counter to set
	 * @throws InterruptedException 
	 */
	public synchronized void setCounter(int counter) throws InterruptedException {
		while(!flag)
		{
			wait();
		}
		//flag=false;
		this.counter = counter;
		this.lastId = counter;
		//System.out.println("The size of the queue: " + queue.size());
		notifyAll();

	}

	/**
	 * @return the parent
	 */
	public Parent<String, InetSocketAddress> getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Parent<String, InetSocketAddress> parent) {
		this.parent = parent;
	}

	public void start() {
		this.running = true;
		Thread t1 = new Thread(cReceiver);
		// t1.setDaemon(true);
		t1.start();

		Thread t2 = new Thread(cHandler);
		// t2.setDaemon(true);
		t2.start();
		Thread t3 = new Thread(sHandler);
		// t2.setDaemon(true);
		t3.start();
		Thread t4 = new Thread(sReceiver);
		// t1.setDaemon(true);
		t4.start();
		Thread t5 = new Thread(dHandler);
		// t1.setDaemon(true);
		t5.start();
	}

	public void registerServer(String sName, InetSocketAddress sAddress) throws DuplicateNameException {
		if (servers.containsKey(sName)) {
			throw new DuplicateNameException();
		}
		servers.put(sName, sAddress);
	}

	public void unregisterServer(String sName) {
		servers.remove(sName);
	}

	public void registerClient(String clientName, InetSocketAddress data_Address, InetSocketAddress signaling_Address)
			throws DuplicateNameException {
		if (clients.containsKey(clientName)) {
			throw new DuplicateNameException();
		}
		clients.put(clientName, data_Address);
		signal_clients.put(clientName, signaling_Address);
	}

	public void unregisterClient(String clientName) {
		clients.remove(clientName);
	}

	public String getPortId() {
		return portId;
	}

	public void registerToServer(InetAddress serverAddress, int commandPort) throws IOException {
		// TODO: Correctly manage exceptions!
		Socket socket = new Socket();
		socket.bind(new InetSocketAddress(this.ssData_socket.getInetAddress(), 0));
		socket.connect(new InetSocketAddress(serverAddress, commandPort));
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer.println("REGISTER\n" + this.portId + "\n" + socket.getLocalAddress().getCanonicalHostName() + "\n"
				+ this.ssData_socket.getLocalPort());
		writer.flush();
		String code = reader.readLine();
		if (AbCServer.OK_MESSAGE.equals(code)) {
			this.ssData_port = Integer.parseInt(reader.readLine());
			this.parentAdress = serverAddress;
			String parentName = reader.readLine();
			this.counter = Integer.parseInt(reader.readLine());
			setParent(new Parent<String, InetSocketAddress>(parentName,
					new InetSocketAddress(this.parentAdress, this.ssData_port)));
			this.registered = true;
		}
		reader.close();
		writer.close();
		socket.close();

	}

	private void dispatch(String Name, NetworkPacket message, InetSocketAddress Address) throws IOException {
		Socket socket = new Socket(Address.getAddress(), Address.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println(gson.toJson(message));
		writer.close();
		socket.close();
	}

	public void RootReply(AbCServer server, NetworkPacket message, MsgType type) throws IOException {
		InetSocketAddress address = server.getServers().get(message.getServerId());
		message.setType(type);
		//System.out.println("RootReply to requester only: " + server.getServers().get(message.getServerId()));
		Socket socket = new Socket(address.getAddress(), address.getPort());
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println(gson.toJson(message));
		writer.close();
		socket.close();
	}

	public void ForwardToParent(AbCServer server, NetworkPacket message, MsgType type) throws IOException {
		Parent<String, InetSocketAddress> parent = server.getParent();
		if (parent != null) {
			InetSocketAddress address = parent.getValue();
			NetworkPacket packet = message;
			packet.setServerId(server.getPortId());
			packet.setType(type);
			Socket socket = new Socket(address.getAddress(), address.getPort());
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println(gson.toJson(packet));
			writer.close();
			socket.close();
		}
	}

	private synchronized void propagate(NetworkPacket pckt, HashMap<String, InetSocketAddress> receivers,
			String senderId) {
		HashMap<String, InetSocketAddress> copyList = new HashMap<>(receivers);
		for (String Name : copyList.keySet()) {
			if (!Name.equals(senderId)) {
				try {
					dispatch(Name, pckt, receivers.get(Name));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void forward(NetworkPacket packet, String serverId) {
		if(!servers.isEmpty())
		propagate(packet, servers, serverId);
	}

	@Override
	public synchronized void receive(NetworkPacket packet) {
		// TODO Auto-generated method stub
		if(!clients.isEmpty())
		propagate(packet, clients, packet.getPacket().getSenderId());
	}

	public class RegistrationHandler implements Runnable {
		ServerSocket svr;

		public RegistrationHandler(ServerSocket svr) {
			this.svr = svr;
		}

		@Override
		public void run() {
			while (running) {
				try {
					// TODO: Correctly manage exceptions!
					Socket socket = svr.accept();

					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					String command = reader.readLine();
					if ("REGISTER".equals(command)) {
						String name = reader.readLine();
						InetAddress host = InetAddress.getByName(reader.readLine());
						int port = Integer.parseInt(reader.readLine());

						try {
							if (svr.equals(cSubscribe_socket)) {
								int signal_port = Integer.parseInt(reader.readLine());
								registerClient(name, new InetSocketAddress(host, port),
										new InetSocketAddress(host, signal_port));
								writer.println(OK_MESSAGE);
								writer.println(csData_socket.getLocalPort());
							} else {
								registerServer(name, new InetSocketAddress(host, port));
								writer.println(OK_MESSAGE);
								writer.println(ssData_socket.getLocalPort());
								writer.println(portId);
								writer.println(counter);
							}
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
						unregisterClient(name);
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

	public class DataHandler implements Runnable {
		@Override
		public void run() {

			while (running) {
				try {
					_Counter();
				} catch (InterruptedException | IOException e) {
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
		cReceiver.stop();
		try {
			cSubscribe_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class PacketComparator implements Comparator<NetworkPacket> {
		@Override
		public int compare(NetworkPacket x, NetworkPacket y) {

			int a = Integer.parseInt(x.getId());
			int b = Integer.parseInt(y.getId());
			if (a < b) {
				return -1;
			}
			if (a > b) {
				return 1;
			}
			return 0;
		}
	}

}
