/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.predicates.And;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.IsGreaterOrEqualThan;
import org.sysma.abc.core.predicates.IsGreaterThan;
import org.sysma.abc.core.predicates.IsLessOrEqualThan;
import org.sysma.abc.core.predicates.IsLessThan;
import org.sysma.abc.core.predicates.Not;
import org.sysma.abc.core.predicates.Or;
import org.sysma.abc.core.predicates.AbCPredicate.PredicateType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class PredicateDeserializer implements JsonDeserializer<AbCPredicate> {

	@Override
	public AbCPredicate deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonObject()) {
			JsonObject o = json.getAsJsonObject();			
			return doDeserialize((AbCPredicate.PredicateType) context.deserialize(o.get("type"),AbCPredicate.PredicateType.class),o,context);
		}
		throw new IllegalStateException("This is not a Message!");
	}

	private AbCPredicate doDeserialize(PredicateType deserialize,
			JsonObject json , JsonDeserializationContext context) {
		switch (deserialize) {
		case TRUE:
			return new TruePredicate();
		case FALSE:
			return new FalsePredicate();
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

	private AbCPredicate doDeserializeNotPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new Not( 
			(AbCPredicate) context.deserialize(json.get("arg"), AbCPredicate.class)
		);
	}

	private AbCPredicate doDeserializeAndPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new And( 
			(AbCPredicate) context.deserialize(json.get("left"), AbCPredicate.class) ,
			(AbCPredicate) context.deserialize(json.get("right"), AbCPredicate.class)
		);
	}

	private AbCPredicate doDeserializeOrPredicate(JsonObject json,
			JsonDeserializationContext context) {
		return new Or( 
			(AbCPredicate) context.deserialize(json.get("left"), AbCPredicate.class) ,
			(AbCPredicate) context.deserialize(json.get("right"), AbCPredicate.class)
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
