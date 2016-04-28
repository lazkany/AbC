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

import org.sysma.abc.core.AbCEnvironment;



/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class FalsePredicate extends AbCPredicate {

	public FalsePredicate() {
		super(AbCPredicate.PredicateType.FALSE);
	}

	@Override
	public boolean isSatisfiedBy(AbCEnvironment store) {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return obj instanceof FalsePredicate;
	}

	@Override
	public int hashCode() {
		return "false".hashCode();
	}

	@Override
	public String toString() {
		return "false";
	}

	@Override
	public AbCPredicate close(AbCEnvironment environment) {
		return this;
	}
	
	

}
