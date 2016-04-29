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
public class ParticipantAgent extends AbCProcess {

	private String selectedTopic;

	public ParticipantAgent( String name , String selectedTopic ) {
		super( name );
		this.selectedTopic = selectedTopic;
	}
	
	@Override
	protected void doRun() throws Exception {
		setValue(SmartConferenceDefinitions.interestAttribute, this.selectedTopic);
		send( 
			new HasValue(
					SmartConferenceDefinitions.ROLE_ATTRIBUTE_NAME, 
					SmartConferenceDefinitions.PROVIDER
			) ,
			new Tuple( 
					getValue(SmartConferenceDefinitions.interestAttribute) ,
					SmartConferenceDefinitions.REQUEST_STRING ,
					getValue(SmartConferenceDefinitions.idAttribute)
					
			)				
		);
		System.out.println(getComponent().getName()+"> registered for topic "+this.selectedTopic);
		Tuple value = (Tuple) receive( o -> isAnInterestReply( o ) );
		setValue(SmartConferenceDefinitions.destinationAttribute, (String) value.get(2));			
		System.out.println(getComponent().getName()+"> moving to room "+getValue(SmartConferenceDefinitions.destinationAttribute));
		while (true) {
			value = (Tuple) receive( o -> isAnInterestUpdate( o ) );
			setValue(SmartConferenceDefinitions.destinationAttribute, (String) value.get(3));			
			System.out.println(getComponent().getName()+"> relocating to room "+getValue(SmartConferenceDefinitions.destinationAttribute));
		}
	}

	private AbCPredicate isAnInterestReply(Object o) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size() == 3)&&
						getValue(SmartConferenceDefinitions.interestAttribute).equals(t.get(0))&&
						SmartConferenceDefinitions.REPLY_STRING.equals(t.get(1)))
						{
							return new TruePredicate();
				}
			} catch (AbCAttributeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new FalsePredicate();
	}

	private AbCPredicate isAnInterestUpdate( Object o ) {
		if (o instanceof Tuple) {
			Tuple t = (Tuple) o;
			try {
				if ((t.size()==4)&&
						(getValue(SmartConferenceDefinitions.interestAttribute).equals(t.get(1)))&&
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
