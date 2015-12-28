/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.NetworkMessages.MsgCentralized;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MsgCenterSerializer implements JsonSerializer<MsgCentralized> {

	@Override
	public JsonElement serialize(MsgCentralized src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.add("msg", context.serialize(src.getMsg()));
		json.add("address", context.serialize(src.getAddress()));
		return json;
	}

}
