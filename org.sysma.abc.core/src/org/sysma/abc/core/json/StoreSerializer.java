/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCEnvironment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class StoreSerializer implements JsonSerializer<AbCEnvironment> {

	@Override
	public JsonElement serialize(AbCEnvironment src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();

		json.add("data", AbCJsonUtil.jsonFromObject(src.getData(),context));
		json.add("attributes", context.serialize(src.getAttributes()));
		return json;
	}

}
