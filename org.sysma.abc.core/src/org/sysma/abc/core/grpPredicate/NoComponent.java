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

import org.sysma.abc.core.AbCStore;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class NoComponent extends GroupPredicate {

	public NoComponent() {
		super(GroupPredicate.PredicateType.FALSE);
	}

	@Override
	public boolean isSatisfiedBy(AbCStore store) {
		return false;
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
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj instanceof NoComponent;
	}

	@Override
	public int hashCode() {
		return "false".hashCode();
	}

	@Override
	public String toString() {
		return "false";
	}

}
