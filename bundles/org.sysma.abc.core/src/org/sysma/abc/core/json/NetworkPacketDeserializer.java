/**
 * 
 */
package org.sysma.abc.core.json;

import java.lang.reflect.Type;

import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.topology.distributed.MsgType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class NetworkPacketDeserializer implements JsonDeserializer<NetworkPacket>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public NetworkPacket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!json.isJsonObject()) {
			throw new JsonParseException("Unexpected JsonElement!");
		}
		
		JsonObject jo = (JsonObject) json;
		
		return new NetworkPacket(
				context.deserialize(jo.get("serverId"), String.class),
				context.deserialize(jo.get("id"), String.class),
				context.deserialize(jo.get("type"), MsgType.class),
				context.deserialize(jo.get("packet"), AbCPacket.class)
				);
		
	}

}
