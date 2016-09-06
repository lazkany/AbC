package org.sysma.abc.core;

import java.util.function.Function;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class OutputAction {
private final Function<AbCEnvironment,AbCEnvironment> localAssignment;
	
	private AbCEnvironment localEnvironment;
	
	private final AbCPredicate guard;
	
	private final Function<Object, AbCPredicate> oPredicate;
	
	public OutputAction( AbCPredicate guard , Function<Object, AbCPredicate> rPredicate ) {
		this( null , guard , rPredicate );
	}
	
	public OutputAction( Function<Object, AbCPredicate> rPredicate ) {
		this( null , null , rPredicate );
	}	
	
	public OutputAction( Function<AbCEnvironment,AbCEnvironment> localEnvironment , AbCPredicate guard , Function<Object, AbCPredicate> rPredicate) {
		this.guard = guard;
		this.oPredicate = rPredicate;
		this.localAssignment = localEnvironment;
	}
	
	public AbCEnvironment deliverMessage( AbCEnvironment store , Object value ) {
		recomputeLocalEnvironment(store);
		try {
			if (isEnabled(store)&&getPredicate(value).isSatisfiedBy(store)) {
				return (localEnvironment==null?new AbCEnvironment():localEnvironment);
			}
		} catch (AbCAttributeTypeException e) {
		}
		return null;
	}

	private boolean isEnabled( AbCEnvironment store ) {		
		if (guard == null) {
			return true;
		}
		AbCPredicate enablingGuard = (localEnvironment==null?guard:guard.close( localEnvironment ));
		try {
			return enablingGuard.isSatisfiedBy( store );
		} catch (AbCAttributeTypeException e) {
			return false;
		}
	}
	
	private void recomputeLocalEnvironment( AbCEnvironment store ) {
		if (localAssignment == null) {
			return ;
		}
		localEnvironment = localAssignment.apply( store );
	}

	private AbCPredicate getPredicate( Object value ) {
		AbCPredicate predicate = oPredicate.apply( value );
		if (localEnvironment != null) {
			predicate = predicate.close(localEnvironment);
		}
		return predicate;
	}

}
