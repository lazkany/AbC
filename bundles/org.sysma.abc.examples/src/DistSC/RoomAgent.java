/**
 * 
 */
package DistSC;

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
			System.out.println( getValue(Defs.nameAttribute)+"> Received: "+value);
			//The following is an asynchronous send. When executed,
			//a new process is created to send the message.
			asend(
				new HasValue(Defs.idAttribute, value.get(2)), 
				new Tuple( 
					getValue(Defs.sessionAttribute) ,  
					Defs.REPLY_STRING ,
					getValue(Defs.nameAttribute)
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
