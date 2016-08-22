/**
 * 
 */
package org.sysma.abc.examples.smartconference;

import java.util.LinkedList;
import java.util.Random;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.AbCPort;
import org.sysma.abc.core.topology.VirtualPort;

/**
 * @author loreti
 *
 */
public class Main {
	
	public static void main( String[] argv ) throws DuplicateNameException, AbCPortException, AbCAttributeTypeException, InterruptedException {
		VirtualPort vp = new VirtualPort();
		AbCComponent r0 = createRoomComponent( 0 , "A" , vp.getPort() );
		AbCComponent r1 = createRoomComponent( 1 , "B" , vp.getPort() );
		AbCComponent r2 = createRoomComponent( 2 , "C" , vp.getPort() );
		AbCComponent p0 = createParticipantComponent( 0 , "A" , vp.getPort() );
		AbCComponent p1 = createParticipantComponent( 1 , "B" , vp.getPort() );
		AbCComponent p2 = createParticipantComponent( 2 , "C" , vp.getPort() );
		r0.start();
		r1.start();
		r2.start();
		Thread.sleep(1000);
		p0.start();
		p1.start();
		p2.start();
		
	}
	
	public static AbCComponent createRoomComponent( int id , String topic , AbCPort port ) throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		AbCComponent c = new AbCComponent("Room "+id);
		c.setValue(Defs.nameAttribute, "Room "+id);
		c.setValue(Defs.relocateAttrivute, false);
		c.setValue(Defs.sessionAttribute, topic);
		c.setValue(Defs.roleAttribute, Defs.PROVIDER);
		port.start();
		c.setPort(port);
		c.addProcess(new RoomAgent());
		c.addProcess(new RelocationAgent());
		c.addProcess(new UpdatingAgent());
		RoomPanel rp = new RoomPanel(c);
		rp.setVisible(true);
		return c;		
	}

	public static AbCComponent createParticipantComponent( int id , String topic , AbCPort port ) throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		AbCComponent c = new AbCComponent("Participant "+id);
		c.setValue(Defs.idAttribute, id);
		port.start();
		c.setPort(port);
		c.addProcess(new ParticipantAgent("Participant "+id,topic));
		return c;		
	}
}
