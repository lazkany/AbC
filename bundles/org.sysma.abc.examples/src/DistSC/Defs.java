/**
 * 
 */
package DistSC;

import org.sysma.abc.core.Attribute;

/**
 * @author loreti
 *
 */
public class Defs {

	public static final String REQUEST_STRING = "S_REQ";
	public static final String UPDATE_STRING = "INTEREST_UPDATE";
	public static final String REPLY_STRING = "INTEREST_REPLY";

	public static final String SESSION_ATTRIBUTE_NAME = "session";
	public static final String INTEREST_ATTRIBUTE_NAME = "interest";
	public static final String ID_ATTRIBUTE_NAME = "id";
	public static final String ROLE_ATTRIBUTE_NAME = "role";
	public static final String DESTINATION_ATTRIBUTE_NAME = "dest";
	public static final String NAME_ATTRIBUTE_NAME = "name";
	public static final String RELOCATE_ATTRIBUTE_NAME = "relocate";
	public static final String PREVIOUS_SESSION_ATTRIBUTE_NAME = "prevSession";
	public static final String NEW_SESSION_ATTRIBUTE_NAME = "newSession";
	public static final String PROVIDER = "Provider";

	public final static Attribute<String> sessionAttribute = new Attribute<>(SESSION_ATTRIBUTE_NAME, String.class);
	public final static Attribute<String> interestAttribute = new Attribute<>(INTEREST_ATTRIBUTE_NAME, String.class);
	public final static Attribute<Integer> idAttribute = new Attribute<>(ID_ATTRIBUTE_NAME, Integer.class);
	public final static Attribute<String> roleAttribute = new Attribute<>(ROLE_ATTRIBUTE_NAME, String.class);
	public static final Attribute<String> destinationAttribute = new Attribute<>(DESTINATION_ATTRIBUTE_NAME, String.class);
	public static final Attribute<String> nameAttribute = new Attribute<>(NAME_ATTRIBUTE_NAME, String.class);
	public static final Attribute<Boolean> relocateAttrivute = new Attribute<>(RELOCATE_ATTRIBUTE_NAME, Boolean.class);
	public static final Attribute<String> previousSessionAttribute = new Attribute<>(PREVIOUS_SESSION_ATTRIBUTE_NAME, String.class);
	public static final Attribute<String> newSessionAttribute = new Attribute<>(NEW_SESSION_ATTRIBUTE_NAME, String.class);
	
}
