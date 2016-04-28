/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCMessage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MessageSerializer implements JsonSerializer<AbCMessage> {

	@Override
	public JsonElement serialize(AbCMessage src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.add("value", AbCJsonUtil.jsonFromObject(src.getValue(), context));
		json.add("predicate", context.serialize(src.getPredicate()));
		return json;
	}

}
