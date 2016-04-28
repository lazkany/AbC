/**
 * 
 */
package org.sysma.abc.examples.smartconference;

import org.sysma.abc.core.Attribute;

/**
 * @author loreti
 *
 */
public class SmartConferenceDefinitions {

	public static final String REQUEST_STRING = "S_REQ";
	public static final String UPDATE_STRING = "INTEREST_UPDATE";
	public static final String REPLY_STRING = "INTEREST_REPLY";

	public static final String TOPIC_ATTRIBUTE_NAME = "interest";
	public static final String ID_ATTRIBUTE_NAME = "id";
	public static final String ROLE_ATTRIBUTE_NAME = "role";
	public static final String DESTINATION_ATTRIBUTE_NAME = "dest";

	public final static Attribute<String> topicAttribute = new Attribute<>(TOPIC_ATTRIBUTE_NAME, String.class);
	public final static Attribute<Integer> idAttribute = new Attribute<>(ID_ATTRIBUTE_NAME, Integer.class);
	public final static Attribute<String> roleAttribute = new Attribute<>(ROLE_ATTRIBUTE_NAME, String.class);
	public static final String PROVIDER = "Provider";
	public static final Attribute<String> destinationAttribute = new Attribute<>(DESTINATION_ATTRIBUTE_NAME, String.class);
	
}
