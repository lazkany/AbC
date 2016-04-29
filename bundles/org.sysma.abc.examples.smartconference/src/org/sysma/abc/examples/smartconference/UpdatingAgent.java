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
			setValue(
					SmartConferenceDefinitions.previousSessionAttribute, 
					getValue(SmartConferenceDefinitions.sessionAttribute));
			setValue(
					SmartConferenceDefinitions.sessionAttribute, 
					(String) value.get(0));
			asend(
					new Or(
						new HasValue(
							SmartConferenceDefinitions.INTEREST_ATTRIBUTE_NAME, 
							getValue(SmartConferenceDefinitions.sessionAttribute)
						) ,
						new HasValue(
								SmartConferenceDefinitions.SESSION_ATTRIBUTE_NAME, 
								getValue(SmartConferenceDefinitions.sessionAttribute)
						)
					), 
					new Tuple(
						getValue( SmartConferenceDefinitions.previousSessionAttribute ) ,
						getValue( SmartConferenceDefinitions.sessionAttribute ) ,
						SmartConferenceDefinitions.UPDATE_STRING ,
						getValue( SmartConferenceDefinitions.nameAttrivute )
					)
			);
		}
	}

	protected AbCPredicate isAnUpdateMessage( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==4)&&
						(getValue(SmartConferenceDefinitions.sessionAttribute).equals(t.get(1)))&&
						(SmartConferenceDefinitions.UPDATE_STRING.equals(t.get(2)))) {
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
