/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.Attribute;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AttributeSerializer implements JsonSerializer<Attribute> {

	@Override
	public JsonElement serialize(Attribute src, Type typeOfSrc,
			JsonSerializationContext context) {
		
		JsonObject json = new JsonObject();
		json.add("name",new JsonPrimitive(src.getName()) );
		json.add("clazz", AbCJsonUtil.jsonFromObject(src.getAttributeType(), context));
		return json;
	}

	

}
