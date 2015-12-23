/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCComponent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class ComponentSerializer implements JsonSerializer<AbCComponent> {

	@Override
	public JsonElement serialize(AbCComponent src, Type typeOfSrc, JsonSerializationContext context) {

		JsonObject json = new JsonObject();

		json.add("name", new JsonPrimitive(src.getName()));
		json.add("store", context.serialize(src.getStore()));
		return json;
	}

}
