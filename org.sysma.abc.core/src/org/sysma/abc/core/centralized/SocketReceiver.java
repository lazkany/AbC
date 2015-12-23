package org.sysma.abc.core.centralized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.abcfactoy.AbCFactory;

import com.google.gson.Gson;

public class SocketReceiver implements Runnable {

	/**
	 * 
	 */
	private ServerSocket ssocket;
	
	private Gson gson = AbCFactory.getGSon();

	private MessageReceiver receiver;

	/**
	 * @param socketPort
	 */
	public SocketReceiver(ServerSocket ssocket , MessageReceiver receiver ) {
		this.ssocket = ssocket;
		this.receiver = receiver;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for messages at "+ssocket.getInetAddress().getCanonicalHostName()+":"+ssocket.getLocalPort());
				Socket s = ssocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				AbCMessage msg = gson.fromJson(reader, AbCMessage.class);
				receiver.receiveMessage(msg);
				reader.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}