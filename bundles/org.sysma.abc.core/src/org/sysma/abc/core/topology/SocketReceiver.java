package org.sysma.abc.core.topology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;

import com.google.gson.Gson;

public class SocketReceiver implements Runnable {

	/**
	 * 
	 */
	private ServerSocket ssocket;

	private Gson gson = AbCFactory.getGSon();

	private PacketReceiver receiver;

	private boolean isRunning = true;
	
	/**
	 * @param socketPort
	 */
	public SocketReceiver(ServerSocket ssocket, PacketReceiver receiver) {
		this.ssocket = ssocket;
		this.receiver = receiver;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				//System.out.println("Waiting for messages at " + ssocket.getInetAddress().getCanonicalHostName() + ":"
				//		+ ssocket.getLocalPort());
				Socket s = ssocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// AbCMessage msg = gson.fromJson(reader, AbCMessage.class);
				AbCPacket msgCentralized = gson.fromJson(reader, AbCPacket.class);

				receiver.receive(msgCentralized);
				reader.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
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

}