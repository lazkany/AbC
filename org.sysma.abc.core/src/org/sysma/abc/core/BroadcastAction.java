/**
 * 
 */
package org.sysma.abc.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
/**
 * @author Yehia Abd Alrahman
 *
 */
public class BroadcastAction extends AbCAction {

	protected GroupPredicate predicate;
	protected Set<Attribute<Object>> s;
	protected Object value;
	protected HashMap<Attribute<Object>,  Object> update;

	@Override
	protected Object doRun(AbCProcess p) throws InterruptedException, IOException, AbCAttributeTypeException {
		return p.Broadcast(predicate, s, value, update);
	}

	public BroadcastAction(GroupPredicate predicate, Set<Attribute<Object>> s, Object value, HashMap<Attribute<Object>,  Object> update) {
		super();
		this.predicate = predicate;
		this.s = s;
		this.value = value;
		this.update = update;
	}

	/* (non-Javadoc)
	 * @see org.sysma.abc.core.AbCAction#getPredicate()
	 */
	@Override
	protected GroupPredicate getPredicate() {
		// TODO Auto-generated method stub
		return predicate;
	}
	/**
	 * @param predicate the predicate to set
	 */
	@Override
	protected void setPredicate(GroupPredicate predicate) {
		this.predicate = predicate;
	}

	/* (non-Javadoc)
	 * @see org.sysma.abc.core.AbCAction#getExposed()
	 */
	@Override
	protected Set<Attribute<Object>> getExposed() {
		// TODO Auto-generated method stub
		return s;
	}
	/**
	 * @return the value
	 */
	@Override
	protected Object getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	@Override
	protected void setValue(Object value) {
		this.value = value;
	}

}