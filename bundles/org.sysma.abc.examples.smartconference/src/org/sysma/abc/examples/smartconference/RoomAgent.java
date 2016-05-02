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
			System.out.println( getValue(Environment.nameAttribute)+"> Received: "+value);
			//The following is an asynchronous send. When executed,
			//a new process is created to send the message.
			asend(
				new HasValue(Environment.ID_ATTRIBUTE_NAME, value.get(2)), 
				new Tuple( 
					getValue(Environment.sessionAttribute) ,  
					Environment.REPLY_STRING ,
					getValue(Environment.nameAttribute)
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
