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
			waitUnil(new HasValue(SmartConferenceDefinitions.RELOCATE_ATTRIBUTE_NAME, true));
			setValue(
					SmartConferenceDefinitions.relocateAttrivute ,
					false);
			setValue(
					SmartConferenceDefinitions.previousSessionAttribute, 
					getValue(SmartConferenceDefinitions.sessionAttribute));
			setValue(
					SmartConferenceDefinitions.sessionAttribute, 
					getValue(SmartConferenceDefinitions.newSessionAttribute));
			send(
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
						getValue( SmartConferenceDefinitions.nameAttribute )
					)
			);
		}
	}

	protected AbCPredicate isARequest( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==3)&&
						(getValue(SmartConferenceDefinitions.sessionAttribute).equals(t.get(0))&&
						(SmartConferenceDefinitions.REQUEST_STRING.equals(t.get(1))))) {
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
