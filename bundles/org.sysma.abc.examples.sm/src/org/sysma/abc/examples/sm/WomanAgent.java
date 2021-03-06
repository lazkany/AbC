/**
 * 
 */
package org.sysma.abc.examples.sm;

import java.util.LinkedList;

import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Not;
import org.sysma.abc.core.predicates.TruePredicate;

/**
 * @author loreti
 *
 */
public class WomanAgent extends AbCProcess {

	public LinkedList<Integer> preferences;
	
	public WomanAgent( LinkedList<Integer> preferences ) {
		super("WomanAgent");
		this.preferences = preferences;
	}
	
	@Override
	protected void doRun() throws Exception {
		System.out.println("Woman started...");
		while ( true ) {
			Tuple msg = (Tuple) receive( o -> isAProposeMessage(o) );
			Integer newPartner = (Integer) msg.get(1);
			if ( bof( this.getValue(Environment.partnerAttribute) , newPartner ) ) {
				Integer previousPartner = getValue(Environment.partnerAttribute);
				setValue(Environment.partnerAttribute, newPartner);
				System.out.println(this.getValue(Environment.idAttribute)+" now married with "+newPartner);

				exec(new AbCProcess() {
					
					@Override
					protected void doRun() throws Exception {
						send( new HasValue(Environment.idAttribute.getName(), previousPartner),new Tuple("INVALID"));
					}
					
				});

			} else {
				exec(new AbCProcess() {
					
					@Override
					protected void doRun() throws Exception {
						send( new HasValue(Environment.idAttribute.getName(), newPartner),new Tuple("INVALID"));
					}
					
				});
			}
//			exec(new PartnerHandler( (Integer) msg.get(1) ));
		}
	}
	
	
	private boolean bof(Integer currentParnter , Integer newPartner) {
		if (currentParnter ==  null)
			return true;
		int pCurrent = preferences.indexOf(currentParnter);
		int pNew = preferences.indexOf(newPartner);
		return pNew<pCurrent;
	}

	private AbCPredicate isAProposeMessage(Object o) {
		if ((o instanceof Tuple)&&("PROPOSE".equals( ((Tuple) o).get(0) ))) {
			return new TruePredicate();
		} else {
			return new FalsePredicate();
		}
	}


}
