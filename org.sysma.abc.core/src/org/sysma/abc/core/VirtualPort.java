/**
 * Copyright (c) 2012 Concurrency and Mobility Group.
 * Universita' di Firenze
 *	
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Michele Loreti
 */
package org.sysma.abc.core;

import java.util.HashMap;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.grpPredicate.And;
import org.sysma.abc.grpPredicate.GroupPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class VirtualPort extends AbCPort {

	private VirtualPortAddress address;

	public VirtualPort(int portId) {
		this.address = new VirtualPortAddress(portId);
	}

	@Override
	protected void send(GroupPredicate predicate, AbCStore store, Object value) {
		HashMap<Object, AbCStore> map = new HashMap<>();
		map.put(value, store);
		for (AbCComponent n : nodes.values()) {
			try {

				if (predicate.isSatisfiedBy(n.getStore())) {
					n.setUpcomming(map);

				}
			} catch (AbCAttributeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Address getAddress() {
		return address;
	}

}
