/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.Attribute;

import com.google.gson.JsonArray;
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
		json.add("attributes",context.serialize(src.getAttributes()));
		return json;
	}

}
