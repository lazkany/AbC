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
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class IsGreaterThan extends GroupPredicate {

	private Number value;
	private String attribute;

	public IsGreaterThan(String attribute, Number value) {
		super(GroupPredicate.PredicateType.ISGTR);
		this.attribute = attribute;
		if (value != null) {
			this.value = value;
		} else {
			this.value = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cmg.resp.topology.GroupPredicate#evaluate(java.util.HashMap)
	 */
	@Override
	public boolean isSatisfiedBy(AbCStore store) throws AbCAttributeTypeException {
		Attribute<?> a = store.getAttribute(attribute);
		Object v = store.getValue(a);
		if (v instanceof Number) {
			return ((Number) v).doubleValue() > value.doubleValue();
		}
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
		String atrString=attribute.substring(1);
		String val=v.toString();
		val = val.replaceAll("\\s+", "");
		int index=Integer.parseInt(atrString);
		String[] ary = val.toString().split(",");
		if (ary[index] == null) {
			return false;
		}
		try {
			if (ary.length-1>=index) {
				return Double.parseDouble(ary[index]) > value.doubleValue();
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
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
			IsGreaterThan p = (IsGreaterThan) obj;
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
		return attribute + ">" + value;
	}

}
