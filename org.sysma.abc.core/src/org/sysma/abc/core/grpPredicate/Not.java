/**
 * Copyright (c) 2013 Concurrency and Mobility Group.
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
package org.sysma.abc.core.grpPredicate;

import java.util.HashMap;

import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class Not extends GroupPredicate {

	private GroupPredicate arg;

	public Not(GroupPredicate arg) {
		super(GroupPredicate.PredicateType.NOT);
		if ((arg == null)) {
			throw new NullPointerException();
		}
		this.arg = arg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cmg.resp.topology.GroupPredicate#evaluate(java.util.HashMap)
	 */
	@Override
	public boolean isSatisfiedBy(AbCStore store) throws AbCAttributeTypeException {
		return !arg.isSatisfiedBy(store);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sysma.abc.core.grpPredicate.GroupPredicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(Object v) {
		// TODO Auto-generated method stub
		return !arg.evaluate(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cmg.resp.topology.GroupPredicate#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (super.equals(obj)) {
			Not p = (Not) obj;
			return arg.equals(p.arg);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ~this.arg.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "!(" + arg.toString() + ")";
	}

	public GroupPredicate getArgument() {
		return arg;
	}

}
