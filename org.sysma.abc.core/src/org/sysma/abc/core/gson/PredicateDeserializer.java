/**
 * 
 */
package org.sysma.abc.core.gson;

import java.lang.reflect.Type;

import org.sysma.abc.core.grpPredicate.And;
import org.sysma.abc.core.grpPredicate.AnyComponent;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.GroupPredicate.PredicateType;
import org.sysma.abc.core.grpPredicate.HasValue;
import org.sysma.abc.core.grpPredicate.IsGreaterOrEqualThan;
import org.sysma.abc.core.grpPredicate.IsGreaterThan;
import org.sysma.abc.core.grpPredicate.IsLessOrEqualThan;
import org.sysma.abc.core.grpPredicate.IsLessThan;
import org.sysma.abc.core.grpPredicate.Not;
import org.sysma.abc.core.grpPredicate.Or;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class PredicateDeserializer implements JsonDeserializer<GroupPredicate> {

	@Override
	public GroupPredicate deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonObject()) {
			JsonObject o = json.getAsJsonObject();			
			return doDeserialize((GroupPredicate.PredicateType) context.deserialize(o.get("type"),GroupPredicate.PredicateType.class),o,context);
		}
		throw new IllegalStateException("This is not a Message!");
	}

	private GroupPredicate doDeserialize(PredicateType deserialize,
			JsonObject json , JsonDeserializationContext context) {
		switch (deserialize) {
		case TRUE:
			return new AnyComponent();
		case ISEQUAL:
			return doDeserializeHasValuePredicate( json , context );
		case ISGTR:
			return doDeserializeIsGreaterThanPredicate( json , context );
		case ISGEQ:
			return doDeserializeIsGreaterOrEqualThanPredicate( json , context );
		case ISLEQ:
			return doDeserializeIsLessOrEqualThanPredicate( json , context );
		case ISLES:
			return doDeserializeIsLessThanPredicate( json , context );
		case AND:
			return doDeserializeAndPredicate( json , context );
		case OR:
			return doDeserializeOrPredicate( json , context );
		case NOT:				
			return doDeserializeNotPredicate( json , context );
		}
		return null;
	}

	private GroupPredicate doDeserializeNotPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new Not( 
			(GroupPredicate) context.deserialize(json.get("arg"), GroupPredicate.class)
		);
	}

	private GroupPredicate doDeserializeAndPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new And( 
			(GroupPredicate) context.deserialize(json.get("left"), GroupPredicate.class) ,
			(GroupPredicate) context.deserialize(json.get("right"), GroupPredicate.class)
		);
	}

	private GroupPredicate doDeserializeOrPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new Or( 
			(GroupPredicate) context.deserialize(json.get("left"), GroupPredicate.class) ,
			(GroupPredicate) context.deserialize(json.get("right"), GroupPredicate.class)
		);
	}

	private IsGreaterOrEqualThan doDeserializeIsGreaterOrEqualThanPredicate(
			JsonObject json, JsonDeserializationContext context) {
		Object o = AbCJsonUtil.objectFromJson(json.get("value"), context);
		if (o instanceof Number) {
			return new IsGreaterOrEqualThan(
					json.get("attribute").getAsString() , 
					(Number) o
				);
		}
		throw new IllegalArgumentException();
	}

	private HasValue doDeserializeHasValuePredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new HasValue(
				json.get("attribute").getAsString() , 
				AbCJsonUtil.objectFromJson(json.get("value"), context)
			);
	}

	private IsGreaterThan doDeserializeIsGreaterThanPredicate(JsonObject json,
			JsonDeserializationContext context) {
		Object o = AbCJsonUtil.objectFromJson(json.get("value"), context);
		if (o instanceof Number) {
			return new IsGreaterThan(
					json.get("attribute").getAsString() , 
					(Number) o
				);
		}
		throw new IllegalArgumentException();
	}

	private IsLessThan doDeserializeIsLessThanPredicate(JsonObject json,
			JsonDeserializationContext context) {
		Object o = AbCJsonUtil.objectFromJson(json.get("value"), context);
		if (o instanceof Number) {
			return new IsLessThan(
					json.get("attribute").getAsString() , 
					(Number) o
				);
		}
		throw new IllegalArgumentException();
	}

	private IsLessOrEqualThan doDeserializeIsLessOrEqualThanPredicate(JsonObject json,
			JsonDeserializationContext context) {
		Object o = AbCJsonUtil.objectFromJson(json.get("value"), context);
		if (o instanceof Number) {
			return new IsLessOrEqualThan(
					json.get("attribute").getAsString() , 
					(Number) o
				);
		}
		throw new IllegalArgumentException();
	}
}
