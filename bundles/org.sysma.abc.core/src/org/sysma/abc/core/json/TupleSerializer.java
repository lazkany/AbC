/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Tuple;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman, Michele Loreti
 *
 */
public class TupleSerializer implements JsonSerializer<Tuple> {

	@Override
	public JsonElement serialize(Tuple src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonArray toReturn = new JsonArray();
		for (int i=0 ; i<src.size() ; i++) {
			toReturn.add(AbCJsonUtil.jsonFromObject(src.get(i), context));
		}
		return toReturn;
	} 	
	
}
