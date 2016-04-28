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
package org.sysma.abc.core.predicates;

import java.io.Serializable;
import java.util.HashMap;

import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;



/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */

public abstract class AbCPredicate implements Serializable {

	public enum PredicateType {		
		FALSE,
		TRUE,
		ISEQUAL,
		ISGTR,
		ISGEQ,
		ISLEQ,
		ISLES,
		AND,
		OR,
		NOT				
	}

	public static final AbCPredicate TRUE = new TruePredicate();
	public static final AbCPredicate FALSE = new FalsePredicate();

	private PredicateType type;
	
	public abstract boolean isSatisfiedBy( AbCEnvironment store ) throws AbCAttributeTypeException;

	public AbCPredicate( PredicateType type ) {
		this.type = type;
	}
	
	public PredicateType getType() {
		return this.type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof AbCPredicate) {
			return this.type == ((AbCPredicate) obj).type;
		}
		return false;
	}

	public abstract AbCPredicate close(AbCEnvironment environment);
	
}
