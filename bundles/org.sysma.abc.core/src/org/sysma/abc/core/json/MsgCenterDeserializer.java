/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MsgCenterDeserializer implements JsonDeserializer<AbCPacket> {

	@Override
	public AbCPacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		
		JsonObject jo = (JsonObject) json;
		
		return new AbCPacket(
				context.deserialize(jo.get("msg"), AbCMessage.class), 
				context.deserialize(jo.get("senderId"), String.class));
		
	}
}


