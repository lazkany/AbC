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
import org.sysma.abc.core.predicates.TruePredicate;

/**
 * @author loreti
 *
 */
public class RoomAgent extends AbCProcess {

	@Override
	protected void doRun() throws Exception {
		while (true) {
			Tuple value = (Tuple) receive(o -> isARequest(o));
			System.out.println( getValue(SmartConferenceDefinitions.nameAttribute)+"> Received: "+value);
			//The following is an asynchronous send. When executed,
			//a new process is created to send the message.
			asend(
				new HasValue(SmartConferenceDefinitions.ID_ATTRIBUTE_NAME, value.get(2)), 
				new Tuple( 
					getValue(SmartConferenceDefinitions.sessionAttribute) ,  
					SmartConferenceDefinitions.REPLY_STRING ,
					getValue(SmartConferenceDefinitions.nameAttribute)
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
