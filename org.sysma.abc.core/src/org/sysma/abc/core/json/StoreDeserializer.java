/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.Attribute;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class StoreDeserializer implements JsonDeserializer<AbCStore> {

	@Override
	public AbCStore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		JsonObject jo = (JsonObject) json;
		if ((!jo.has("data"))) {
			throw new JsonParseException("Required data are not available!");
		}

//		return new AbCStore((HashMap<String, Object>) AbCJsonUtil.objectFromJson(jo.get("data"), context),
//				(HashMap<String, Attribute<?>>) AbCJsonUtil.objectFromJson(jo.get("attributes"), context));
		return new AbCStore((HashMap<String, Object>) context.deserialize(jo.get("data"), HashMap.class),
				(HashMap<String, Attribute<?>>) context.deserialize(jo.get("attributes"), HashMap.class));

	}

}
