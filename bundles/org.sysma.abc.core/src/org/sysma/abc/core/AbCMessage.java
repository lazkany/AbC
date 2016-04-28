/**
 * 
 */
package org.sysma.abc.core;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;

/**
 * @author Michele Loreti
 * @author Yehia Abd Alrahman
 */
public class AbCMessage {

	private Object value;

	private AbCPredicate predicate;


	public AbCMessage(Object value, AbCPredicate predicate) {
		this.value = value;
		this.predicate = predicate;
		
	}

	public boolean isAReceiverFor(AbCEnvironment store) {
		try {
			return predicate.isSatisfiedBy(store);
		} catch (AbCAttributeTypeException e) {
			return false;
		}
	}

	public Object getValue() {
		return value;
	}

	public AbCPredicate getPredicate() {
		return predicate;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbCMessage [value=" + (value==null?"_":value) + ", predicate=" + predicate + "  ]";
	}

	@Override
	public int hashCode() {
		return (value==null?0:value.hashCode())+predicate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbCMessage) {
			AbCMessage other = (AbCMessage) obj;
			return (value==null?other.value==null:value.equals(other.value))
					&&(predicate.equals(other.predicate));
		}
		return false;
	}


}
