/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;
import java.net.ServerSocket;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Address;
import org.sysma.abc.core.NetworkMessages.MsgCentralized;
import org.sysma.abc.core.centralized.ServerPortAddress;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class MsgCenterDeserializer implements JsonDeserializer<MsgCentralized> {

	@Override
	public MsgCentralized deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		
		JsonObject jo = (JsonObject) json;
		
		return  new MsgCentralized(context.deserialize(jo.get("msg"), AbCMessage.class), (ServerPortAddress)context.deserialize(jo.get("address"), ServerPortAddress.class));
		
}
}


