/**
 * 
 */
package org.sysma.abc.examples.smartconference;

import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Or;
import org.sysma.abc.core.predicates.TruePredicate;

/**
 * @author loreti
 *
 */
public class RelocationAgent extends AbCProcess {

	@Override
	protected void doRun() throws Exception {
		while (true) {
			waitUnil(new HasValue(Environment.RELOCATE_ATTRIBUTE_NAME, true));
			setValue(
					Environment.relocateAttrivute ,
					false);
			setValue(
					Environment.previousSessionAttribute, 
					getValue(Environment.sessionAttribute));
			setValue(
					Environment.sessionAttribute, 
					getValue(Environment.newSessionAttribute));
			send(
					new Or(
						new HasValue(
							Environment.INTEREST_ATTRIBUTE_NAME, 
							getValue(Environment.sessionAttribute)
						) ,
						new HasValue(
								Environment.SESSION_ATTRIBUTE_NAME, 
								getValue(Environment.sessionAttribute)
						)
					), 
					new Tuple(
						getValue( Environment.previousSessionAttribute ) ,
						getValue( Environment.sessionAttribute ) ,
						Environment.UPDATE_STRING ,
						getValue( Environment.nameAttribute )
					)
			);
		}
	}

	protected AbCPredicate isARequest( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==3)&&
						(getValue(Environment.sessionAttribute).equals(t.get(0))&&
						(Environment.REQUEST_STRING.equals(t.get(1))))) {
					return new TruePredicate();
				}
			} catch (AbCAttributeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new FalsePredicate();
	}

}
