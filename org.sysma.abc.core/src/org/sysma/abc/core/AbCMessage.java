/**
 * 
 */
package org.sysma.abc.core;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

/**
 * @author loreti
 *
 */
public class AbCMessage {

	private AbCComponent sender;

	private Object value;

	private GroupPredicate predicate;

	private AbCStore store;

	public AbCMessage(AbCComponent sender, Object value, GroupPredicate predicate, AbCStore store) {
		this.sender = sender;
		this.value = value;
		this.predicate = predicate;
		this.store = store;
	}

	public boolean isAReceiverFor(AbCStore store) {
		try {
			return predicate.isSatisfiedBy(store);
		} catch (AbCAttributeTypeException e) {
			return false;
		}
	}

	public Object getValue(GroupPredicate predicate) {
		try {
			if (predicate.isSatisfiedBy(store)) {
				return value;
			}
		} catch (AbCAttributeTypeException e) {
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
		return "AbCMessage [sender=" + sender + ", value=" + value + ", predicate=" + predicate + ", store=" + store
				+ "]";
	}

}
