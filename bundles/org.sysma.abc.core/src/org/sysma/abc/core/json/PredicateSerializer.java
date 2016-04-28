/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.predicates.And;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.IsGreaterOrEqualThan;
import org.sysma.abc.core.predicates.IsGreaterThan;
import org.sysma.abc.core.predicates.IsLessOrEqualThan;
import org.sysma.abc.core.predicates.IsLessThan;
import org.sysma.abc.core.predicates.Not;
import org.sysma.abc.core.predicates.Or;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class PredicateSerializer implements JsonSerializer<AbCPredicate>{

	@Override
	public JsonElement serialize(AbCPredicate src, Type typeOfSrc,
			JsonSerializationContext context) {
		AbCPredicate.PredicateType gType = src.getType();
		JsonObject json = new JsonObject();
		json.add("type", context.serialize(gType, AbCPredicate.PredicateType.class));
		switch (gType) {
			case TRUE:
				return json;
			case FALSE:
				return json;
			case ISEQUAL:
				return doSerializeHasValuePredicate( (HasValue) src , json , context );
			case ISGTR:
				return doSerializeIsGreaterThanPredicate( (IsGreaterThan) src , json , context );
			case ISGEQ:
				return doSerializeIsGreaterOrEqualThanPredicate( (IsGreaterOrEqualThan) src , json , context );
			case ISLEQ:
				return doSerializeIsLessOrEqualThanPredicate( (IsLessOrEqualThan) src , json , context );
			case ISLES:
				return doSerializeIsLessThanPredicate( (IsLessThan) src , json , context );
			case AND:
				return doSerializeAndPredicate( (And) src , json , context );
			case OR:
				return doSerializeOrPredicate( (Or) src , json , context );
			case NOT:				
				return doSerializeNotPredicate( (Not) src, json, context);
			}
		return json;
	}

	private JsonElement doSerializeNotPredicate(Not src, JsonObject json,
			JsonSerializationContext context) {
		json.add("arg", context.serialize(src.getArgument() , AbCPredicate.class));
		return json;
	}

	private JsonElement doSerializeAndPredicate(And src, JsonObject json,
			JsonSerializationContext context) {
		json.add("left", context.serialize(src.getLeft(), AbCPredicate.class));
		json.add("right", context.serialize(src.getRight(), AbCPredicate.class));
		return json;
	}

	private JsonElement doSerializeOrPredicate(Or src, JsonObject json,
			JsonSerializationContext context) {
		json.add("left", context.serialize(src.getLeft(), AbCPredicate.class));
		json.add("right", context.serialize(src.getRight(), AbCPredicate.class));
		return json;
	}

	private JsonElement doSerializeIsGreaterOrEqualThanPredicate(
			IsGreaterOrEqualThan src, JsonObject json, JsonSerializationContext context) {
		json.addProperty("attribute", src.getAttribute());
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context) );
		return json;
	}

	private JsonElement doSerializeIsGreaterThanPredicate(IsGreaterThan src,
			JsonObject json, JsonSerializationContext context) {
		json.addProperty("attribute", src.getAttribute());
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context) );
		return json;
	}

	private JsonElement doSerializeIsLessThanPredicate(IsLessThan src,
			JsonObject json, JsonSerializationContext context) {
		json.addProperty("attribute", src.getAttribute());
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context) );
		return json;
	}

	private JsonElement doSerializeIsLessOrEqualThanPredicate(IsLessOrEqualThan src,
			JsonObject json, JsonSerializationContext context) {
		json.addProperty("attribute", src.getAttribute());
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context) );
		return json;
	}

	
	private JsonElement doSerializeHasValuePredicate(HasValue src, JsonObject json,
			JsonSerializationContext context) {
		json.addProperty("attribute", src.getAttribute());
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context) );
		return json;
	}
}
