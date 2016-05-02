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
public class UpdatingAgent extends AbCProcess {

	@Override
	protected void doRun() throws Exception {
		while (true) {
			Tuple value = (Tuple) receive( o -> isAnUpdateMessage( o ) );
			System.out.println( getValue(Environment.nameAttribute)+"> Received: "+value);
			setValue(
					Environment.previousSessionAttribute, 
					getValue(Environment.sessionAttribute));
			setValue(
					Environment.sessionAttribute, 
					(String) value.get(0));
			System.out.println( 
					getValue(Environment.nameAttribute)+"> New session: "+
							getValue(Environment.previousSessionAttribute)
							+"->"+
							getValue(Environment.sessionAttribute));
			asend(
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

	protected AbCPredicate isAnUpdateMessage( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==4)&&
						(getValue(Environment.sessionAttribute).equals(t.get(1)))&&
						(Environment.UPDATE_STRING.equals(t.get(2)))) {
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
