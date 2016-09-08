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
			System.out.println( getValue(Defs.nameAttribute)+"> Received: "+value);
			setValue(
					Defs.previousSessionAttribute, 
					getValue(Defs.sessionAttribute));
			setValue(
					Defs.sessionAttribute, 
					(String) value.get(0));
			System.out.println( 
					getValue(Defs.nameAttribute)+"> New session: "+
							getValue(Defs.previousSessionAttribute)
							+"->"+
							getValue(Defs.sessionAttribute));
			asend(
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

	protected AbCPredicate isAnUpdateMessage( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==4)&&
						(getValue(Defs.sessionAttribute).equals(t.get(1)))&&
						(Defs.UPDATE_STRING.equals(t.get(2)))) {
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
