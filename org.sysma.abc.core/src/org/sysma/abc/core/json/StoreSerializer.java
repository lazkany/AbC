/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCStore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class StoreSerializer implements JsonSerializer<AbCStore> {

	@Override
	public JsonElement serialize(AbCStore src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();

		json.add("data", context.serialize(src.getData()));
		json.add("attributes", context.serialize(src.getAttributes()));
		return json;
	}

}
