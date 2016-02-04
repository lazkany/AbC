/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.grpPredicate.GroupPredicate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MessageDeserializer implements JsonDeserializer<AbCMessage> {

	@Override
	public AbCMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		JsonObject jo = (JsonObject) json;

		return new AbCMessage(context.deserialize(jo.get("sender"), AbCComponent.class),
				context.deserialize(jo.get("value"),Object.class),
				context.deserialize(jo.get("predicate"), GroupPredicate.class));

	}

}
