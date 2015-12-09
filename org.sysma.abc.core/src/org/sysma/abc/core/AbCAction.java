/**
 * 
 */
package org.sysma.abc.core;

import java.io.IOException;
import java.util.Set;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class AbCAction {
	protected abstract Object doRun(AbCProcess p) throws InterruptedException, IOException, AbCAttributeTypeException;

	/**
	 * @return
	 */
	protected abstract GroupPredicate getPredicate() ;

	/**
	 * @return
	 */
	protected Set<Attribute<Object>> getExposed() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param predicate
	 */
	protected void setPredicate(GroupPredicate predicate) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	protected Object getValue() {
		return null;
	}
	/**
	 * @param value the value to set
	 */
	protected void setValue(Object value) {
		
	}

//	public final boolean execute(AbCProcess p) throws InterruptedException, IOException, AbCAttributeTypeException {
//		return doRun(p);
//	}


//	public static class Execute extends AbCAction {
//
//		protected AbCProcess process;
//
//		@Override
//		protected boolean doRun(AbCProcess p) throws InterruptedException, IOException {
//			p.call(process);
//			return (true);
//		}
//
//		public Execute(AbCProcess p) {
//			super();
//			this.process = p;
//		}
//
//	}
}
