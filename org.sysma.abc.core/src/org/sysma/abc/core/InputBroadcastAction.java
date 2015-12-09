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
public class InputBroadcastAction extends AbCAction {

	protected GroupPredicate predicate;

	protected Object value;
	
	protected HashMap<Attribute<Object>, Object> update;

	@Override
	protected Object doRun(AbCProcess p) throws InterruptedException, IOException, AbCAttributeTypeException {
		return p.receive(predicate, value, update);
	}
	/**
	 * @return the predicate
	 */
	@Override
	protected GroupPredicate getPredicate() {
		return predicate;
	}
	/**
	 * @param predicate the predicate to set
	 */
	@Override
	protected void setPredicate(GroupPredicate predicate) {
		this.predicate = predicate;
	}
	/**
	 * @return the value
	 */
	@Override
	protected synchronized Object getValue() {
		while (value==null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	 * @param value the value to set
	 */
	@Override
	protected synchronized void setValue(Object value) {
		this.value = value;
		notify();
	}


	public InputBroadcastAction(GroupPredicate predicate, HashMap<Attribute<Object>, Object> update) {
		super();
		this.predicate = predicate;
		this.value = null;
		this.update = update;
	}
	/* (non-Javadoc)
	 * @see org.sysma.abc.core.AbCAction#getExposed()
	 */
	

}