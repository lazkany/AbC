/**
 * 
 */
package org.sysma.abc.core;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

/**
 * @author loreti
 * @author Yehia Abd Alrahman
 */
public class AbCMessage {

	private AbCComponent sender;

	private Object value;

	private GroupPredicate predicate;

	

	public AbCMessage(AbCComponent sender, Object value, GroupPredicate predicate) {
		this.sender = sender;
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

	public Object getValue(GroupPredicate predicate) {
		if (predicate.evaluate(value)) {
			return value;
		}
		return null;
	}

	public AbCComponent getSender() {
		return sender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbCMessage [sender=" + sender + ", value=" + value + ", predicate=" + predicate + ", store="
				+ "]";
	}

}