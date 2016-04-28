/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.NetworkMessages.AbCPacket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MsgCenterSerializer implements JsonSerializer<AbCPacket> {

	@Override
	public JsonElement serialize(AbCPacket src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.add("msg", context.serialize(src.getMessage()));
		json.add("senderId", context.serialize(src.getSenderId()));
		return json;
	}

}
