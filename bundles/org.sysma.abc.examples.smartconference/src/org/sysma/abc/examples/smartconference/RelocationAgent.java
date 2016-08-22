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
			waitUnil(new HasValue(Defs.relocateAttrivute, true));
			setValue(
					Defs.relocateAttrivute ,
					false);
			setValue(
					Defs.previousSessionAttribute, 
					getValue(Defs.sessionAttribute));
			setValue(
					Defs.sessionAttribute, 
					getValue(Defs.newSessionAttribute));
			send(
					new Or(
						new HasValue(
							Defs.interestAttribute, 
							getValue(Defs.sessionAttribute)
						) ,
						new HasValue(
								Defs.sessionAttribute, 
								getValue(Defs.sessionAttribute)
						)
					), 
					new Tuple(
						getValue( Defs.previousSessionAttribute ) ,
						getValue( Defs.sessionAttribute ) ,
						Defs.UPDATE_STRING ,
						getValue( Defs.nameAttribute )
					)
			);
		}
	}

	protected AbCPredicate isARequest( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==3)&&
						(getValue(Defs.sessionAttribute).equals(t.get(0))&&
						(Defs.REQUEST_STRING.equals(t.get(1))))) {
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
