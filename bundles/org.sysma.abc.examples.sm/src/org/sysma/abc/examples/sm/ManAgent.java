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
import org.sysma.abc.core.predicates.TruePredicate;

/**
 * @author loreti
 *
 */
public class ManAgent extends AbCProcess {

	public LinkedList<Integer> preferences;
	
	public ManAgent( LinkedList<Integer> preferences ) {
		super("ManAgent");
		this.preferences = preferences;
	}
	
	@Override
	protected void doRun() throws Exception {
		System.out.println("Man started...");
		while ( !preferences.isEmpty() ) {
			Integer partner = preferences.poll();
			System.out.println(getValue(Environment.idAttribute)+"> Selected partner: "+partner);
			setValue(Environment.partnerAttribute, partner);
			System.out.println(getValue(Environment.idAttribute)+"> Sendig request to "+partner);
			send( new HasValue("ID", partner) , new Tuple( "PROPOSE" , getValue(Environment.idAttribute)) );
			receive(o -> isAnInvalidatingMessage(o) );
			System.out.println(getValue(Environment.idAttribute)+"> Matching changed!");
		}
		System.out.println(getValue(Environment.idAttribute)+"> I am alone...");
	}
	
	
	public AbCPredicate isAnInvalidatingMessage( Object msg ) {
		if (msg instanceof Tuple) {
			Tuple t = (Tuple) msg;
			if ((t.size() == 1)&&(t.get(0).equals("INVALID"))) {
				return new TruePredicate();
			}
		}
		return new FalsePredicate();
	}

}
