/**
 * 
 */
package org.sysma.abc.core;

import java.util.Hashtable;

import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AbCPort {

	protected Hashtable<String, AbCComponent> nodes;

	/**
	 * Constructs a new <code>AbstractPort</code>.
	 */
	public AbCPort() {
		this.nodes = new Hashtable<String, AbCComponent>();
	}

	/**
	 * @param abCComponent
	 * @throws DuplicateNameException
	 */

	public synchronized void register(AbCComponent comp) throws DuplicateNameException {
		if (nodes.contains(comp.getName())) {
			throw new DuplicateNameException();
		}
		nodes.put(comp.getName(), comp);
	}

	/**
	 * @return
	 */
	public Address getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	protected void send(GroupPredicate predicate, AbCStore store, Object value) {
		// TODO Auto-generated method stub
		
	}

	
	

}
