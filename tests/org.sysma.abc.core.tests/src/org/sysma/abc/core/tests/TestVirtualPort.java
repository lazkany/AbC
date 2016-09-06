/**
 * 
 */
package org.sysma.abc.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.distributed.MessageReceiver;
import org.sysma.abc.core.topology.VirtualPort;

/**
 * @author loreti
 *
 */
public class TestVirtualPort {
	
	@Test
	public void testCreateStartAndStop() {
		VirtualPort vp = new VirtualPort();
		assertFalse(vp.isRunning());
		vp.start();
		assertTrue(vp.isRunning());
		vp.stop();
		assertFalse(vp.isRunning());
	}

	@Test
	public void testSend() throws InterruptedException, AbCPortException {
		VirtualPort vp = new VirtualPort();
		vp.start();
		Integer test = 10;
		Object[] received = new Object[1];
		AbCPort lp1 = vp.getPort();
		lp1.setReceiver( new MessageReceiver() {
			
			@Override
			public void receive(AbCMessage message) {
				synchronized (test) {
					received[0] = message.getValue();
					test.notifyAll();
				}
			}
			
		});
		AbCPort lp2 = vp.getPort();
		lp2.setReceiver( new MessageReceiver() {
			
			@Override
			public void receive(AbCMessage message) {
				synchronized (test) {
					assertTrue( false );
				}
			}
			
		});
		lp1.start();
		lp2.start();
		lp2.connect().send(new AbCMessage(test, new TruePredicate()));
		synchronized (test) {
			while (received[0]==null) {
				test.wait();
			}
		}
		assertEquals(test, received[0]);
	}
	
	
}
