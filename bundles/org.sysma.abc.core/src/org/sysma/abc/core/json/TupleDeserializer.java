/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.AbCEnvironment;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class TupleDeserializer implements JsonDeserializer<Tuple> {

	@Override
	public Tuple deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (!json.isJsonArray()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		JsonArray jsa = (JsonArray) json;
		Object[] data = new Object[jsa.size()];
		for( int i=0 ; i<jsa.size() ; i++ ) {
			data[i] = AbCJsonUtil.objectFromJson(jsa.get(i), context);
		}
		return new Tuple(data);
	}
	
}
