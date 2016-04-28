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
package org.sysma.abc.core.predicates;

import java.util.HashMap;

import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class Or extends AbCPredicate {

	private AbCPredicate left;
	private AbCPredicate right;

	public Or(AbCPredicate left, AbCPredicate right) {
		super(AbCPredicate.PredicateType.OR);
		if ((left == null) || (right == null)) {
			throw new NullPointerException();
		}
		this.left = left;
		this.right = right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cmg.resp.topology.GroupPredicate#evaluate(java.util.HashMap)
	 */
	@Override
	public boolean isSatisfiedBy(AbCEnvironment store) throws AbCAttributeTypeException {
		return left.isSatisfiedBy(store) || right.isSatisfiedBy(store);
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
			Or p = (Or) obj;
			return left.equals(p.left) && right.equals(p.right);
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
		return this.left.hashCode() ^ this.right.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + left.toString() + ") || (" + right.toString() + ")";
	}

	public AbCPredicate getLeft() {
		return left;
	}

	public AbCPredicate getRight() {
		return right;
	}

	@Override
	public AbCPredicate close(AbCEnvironment environment) {
		return new Or(left.close(environment), right.close(environment)	);
	}
}
