/**
 * 
 */
package org.sysma.abc.examples.sm;

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
		AbCComponent m0 = createManComponent(0, new Integer[] { 3 , 4 , 5} , vp.getPort() );
		AbCComponent m1 = createManComponent(1, new Integer[] { 3 , 4 , 5} , vp.getPort() );		
		AbCComponent m2 = createManComponent(2, new Integer[] { 3 , 4 , 5} , vp.getPort() );
		AbCComponent w0 = createWomanComponent(3, new Integer[] { 2 , 1 , 0} , vp.getPort() );
		AbCComponent w1 = createWomanComponent(4, new Integer[] { 1 , 2 , 0} , vp.getPort() );		
		AbCComponent w2 = createWomanComponent(5, new Integer[] { 2 , 1 , 0} , vp.getPort() );
		w2.start();
		w1.start();
		w0.start();
		Thread.sleep(1000);
		m0.start();
		m1.start();
		m2.start();
		
	}
	
	public static AbCComponent createManComponent( int id , Integer[] integers , AbCPort port ) throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		LinkedList<Integer> preferences = new LinkedList<>();
		for( int i=0 ;i<integers.length ;i++) {
			preferences.add(integers[i]);
		}
		AbCComponent c = new AbCComponent("M"+id);
		c.setValue(SmDefinitions.idAttribute, id);
		port.start();
		c.setPort(port);
		c.addProcess(new ManAgent(preferences));
		return c;		
	}

	public static AbCComponent createWomanComponent( int id , Integer[] integers , AbCPort port ) throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		LinkedList<Integer> preferences = new LinkedList<>();
		for( int i=0 ;i<integers.length ;i++) {
			preferences.add(integers[i]);
		}
		AbCComponent c = new AbCComponent("W"+id);
		c.setValue(SmDefinitions.idAttribute, id);
		port.start();
		c.setPort(port);
		c.addProcess(new WomanAgent(preferences));
		return c;		
	}

}
