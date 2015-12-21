/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;


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
public class  AttributeDeserializer<T> implements JsonDeserializer<Attribute<T>> {

	@Override
	public  Attribute deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
//	//	Type attributeType = new TypeToken<Attribute<?>>() {}.getType();
//		if (!json.isJsonObject()) {
//			throw new JsonParseException("Unexpected JsonElement!");
//		}
//		JsonObject jo = (JsonObject) json;
////		if ((!jo.has("name"))||(!jo.has("value"))) {
////			throw new JsonParseException("Required attributes are not available!");
////		}
//		
//		return new Attribute(jo.get("name").getAsString(), (Class<T>) AbCJsonUtil.objectFromJson(jo.get("clazz"), context));
//		
		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		JsonObject jo = (JsonObject) json;
		if ((!jo.has("clazz"))) {
			throw new JsonParseException("Required attributes are not available!");
		}		
		try {
			Class<?> c = Class.forName(jo.get("clazz").getAsString());
			return new Attribute<>(jo.get("name").getAsString(), c);
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}		
	}


}

