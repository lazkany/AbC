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
package org.sysma.abc.core.grpPredicate;

import java.util.HashMap;

import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class HasValue extends GroupPredicate {

	private Object value;

	private String attribute;

	public HasValue(String attribute, Object value) {
		super(GroupPredicate.PredicateType.ISEQUAL);
		this.attribute = attribute;
		this.value = value;
	}

	@Override
	public boolean isSatisfiedBy(AbCStore store) throws AbCAttributeTypeException {
		Attribute<?> a = store.getAttribute(attribute);
		if (a == null) {
			return false;
		}
		Object o = store.getValue(a);
		return (value == o) || ((value != null) && (value.equals(o)));
	}

	
	@Override
	public boolean evaluate(Object v) {
		// TODO Auto-generated method stub
		//this.attribute=this.attribute.replaceAll("$", "");
//		String atrString="";
//		atrString=attribute.toString();
		String atrString=attribute.substring(1);
		String val=v.toString();
		val = val.replaceAll("\\s+", "");
		int index=Integer.parseInt(atrString);
		String[] ary = val.toString().split(",");
		if (ary.length-1>=index) {
			return (value == ary[index]) || ((value != null) && (value.equals(ary[index])));
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof HasValue) {
			HasValue hv = (HasValue) obj;
			return attribute.equals(hv.attribute) && value.equals(hv.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return attribute.hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() {
		return "{" + attribute + "==" + value + "}";
	}

	public String getAttribute() {
		return attribute;
	}

	public Object getValue() {
		return value;
	}

}
