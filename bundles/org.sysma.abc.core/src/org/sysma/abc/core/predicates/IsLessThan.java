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
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class IsLessThan extends AbCPredicate {

	private Number value;
	private String attribute;

	public IsLessThan(String attribute, Number value) {
		super(AbCPredicate.PredicateType.ISLES);
		this.attribute = attribute;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cmg.resp.topology.GroupPredicate#evaluate(java.util.HashMap)
	 */
	@Override
	public boolean isSatisfiedBy(AbCEnvironment store) throws AbCAttributeTypeException {
		Attribute<?> a = store.getAttribute(attribute);
		if (a == null) {
			return false;
		}
		Object v = store.getValue(a);
		if (v instanceof Number) {
			return ((Number) v).doubleValue() < value.doubleValue();
		}
		return false;
	}

	public String getAttribute() {
		return attribute;
	}

	public Number getValue() {
		return value;
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
			IsLessThan p = (IsLessThan) obj;
			if (!this.attribute.equals(p.attribute)) {
				return false;
			}
			return (this.value == p.value) || ((this.value != null) && (this.value.equals(p.value)));
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
		return attribute.hashCode() ^ (value == null ? 0 : value.hashCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return attribute + "<" + value;
	}
	
	@Override
	public AbCPredicate close(AbCEnvironment environment) {
		Object o = environment.getValue(attribute);
		if (o == null) {
			return this;
		}
		try {
			if (this.isSatisfiedBy(environment)) {
				return AbCPredicate.TRUE;
			} else {
				return AbCPredicate.FALSE;
			}
		} catch (AbCAttributeTypeException e) {
			return this;
		}
	}
}
