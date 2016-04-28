/**
 * 
 */
package org.sysma.abc.core.tests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.AbCClient;
import org.sysma.abc.core.topology.AbCPort;
import org.sysma.abc.core.topology.AbCServer;
import org.sysma.abc.core.topology.MessageReceiver;

/**
 * @author loreti
 *
 */
public class TestAbCServer {
	
	@Test
	public void testStartAndStop() throws IOException {
		AbCServer server = new AbCServer();
		assertFalse( server.isRunning() );
		server.start();
		assertTrue( server.isRunning() );
		server.stop( );
		assertFalse( server.isRunning() );
	}

	@Test
	public void testConnect() throws IOException, AbCPortException {
		AbCServer server = new AbCServer();
		server.start();
		AbCClient client = new AbCClient( InetAddress.getLoopbackAddress() , 8888 );
		client.register(InetAddress.getByName("127.0.0.1"), AbCServer.DEFAULT_SUBSCRIBE_PORT);
		assertTrue( true );
		server.stop();
	}

	@Test
	public void testSend() throws IOException, InterruptedException, AbCPortException {
		AbCServer server = new AbCServer();
		server.start();
		Integer test = 10;
		Object[] received = new Object[1];
		AbCClient client1 = new AbCClient( InetAddress.getLoopbackAddress() , 8888 );
		client1.setReceiver( new MessageReceiver() {
			
			@Override
			public void receive(AbCMessage message) {
				synchronized (test) {
					received[0] = message.getValue();
					test.notifyAll();
				}
			}
			
		});
		AbCClient client2 = new AbCClient( InetAddress.getLoopbackAddress() , 8889 );
		client1.register(InetAddress.getByName("127.0.0.1"), AbCServer.DEFAULT_SUBSCRIBE_PORT);
		client2.register(InetAddress.getByName("127.0.0.1"), AbCServer.DEFAULT_SUBSCRIBE_PORT);
		client1.start();
		client2.start();
		client2.connect().send( new AbCMessage(test, new TruePredicate()) );
		synchronized (test) {
			while (received[0]==null) {
				test.wait();
			}
		}
		client1.stop();
		client2.stop();
		server.stop();
		assertEquals(test, received[0]);
	}

	
}
